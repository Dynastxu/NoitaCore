package dynastxu.noitacore.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.common.spell.DamageType;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.common.spell.SuffixType;
import dynastxu.noitacore.utils.EnumCodecs;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.NonNull;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * @see <a href="https://noita.wiki.gg/wiki/Spells">wiki</a>
 */
@Builder(toBuilder = true)
public record SpellAttributes(
        @NonNull BaseAttributes base,
        @Nullable Modifications modifications,
        @Nullable Damage damage,
        @Nullable Time time,
        @Nullable Suffix suffix,
        @Nullable Motion motion,
        @Nullable Other other
) {
    public static final Codec<SpellAttributes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BaseAttributes.CODEC.fieldOf("base").forGetter(SpellAttributes::base),
            Modifications.CODEC.optionalFieldOf("modifications").forGetter(sa -> Optional.ofNullable(sa.modifications)),
            Damage.CODEC.optionalFieldOf("damage").forGetter(sa -> Optional.ofNullable(sa.damage)),
            Time.CODEC.optionalFieldOf("time").forGetter(sa -> Optional.ofNullable(sa.time)),
            Suffix.CODEC.optionalFieldOf("suffix").forGetter(sa -> Optional.ofNullable(sa.suffix)),
            Motion.CODEC.optionalFieldOf("motion").forGetter(sa -> Optional.ofNullable(sa.motion)),
            Other.CODEC.optionalFieldOf("other").forGetter(sa -> Optional.ofNullable(sa.other))
    ).apply(instance, (base, mod, dmg, time, sfx, motion, other) ->
            new SpellAttributes(base,
                    mod.orElse(null),
                    dmg.orElse(null),
                    time.orElse(null),
                    sfx.orElse(null),
                    motion.orElse(null),
                    other.orElse(null))));

    public boolean isModifier() {
        return base.type == SpellType.Modifier;
    }

    public boolean isMulticast() {
        return base.type == SpellType.Multicast;
    }

    public record BaseAttributes(
            @NonNull SpellType type,
            @NonNull Uses uses,
            int manaDrain,
            int castDelayTick,
            int rechargeTick,
            int draws
    ) {
        public static final Codec<BaseAttributes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EnumCodecs.codec(SpellType.class).fieldOf("type").forGetter(BaseAttributes::type),
                Uses.CODEC.fieldOf("uses").forGetter(BaseAttributes::uses),
                Codec.INT.fieldOf("mana_drain").forGetter(BaseAttributes::manaDrain),
                Codec.INT.fieldOf("cast_delay_tick").forGetter(BaseAttributes::castDelayTick),
                Codec.INT.fieldOf("recharge_tick").forGetter(BaseAttributes::rechargeTick),
                Codec.INT.fieldOf("draws").forGetter(BaseAttributes::draws)
        ).apply(instance, BaseAttributes::new));

        public static final StreamCodec<ByteBuf, BaseAttributes> STREAM_CODEC = StreamCodec.composite(
                EnumCodecs.streamCodec(SpellType.class),  BaseAttributes::type,
                Uses.STREAM_CODEC, BaseAttributes::uses,
                ByteBufCodecs.VAR_INT, BaseAttributes::manaDrain,
                ByteBufCodecs.VAR_INT, BaseAttributes::castDelayTick,
                ByteBufCodecs.VAR_INT, BaseAttributes::rechargeTick,
                ByteBufCodecs.VAR_INT, BaseAttributes::draws,
                BaseAttributes::new
        );
    }

    public record Uses(
            int uses,
            boolean mustConsume
    ) {
        public static final Codec<Uses> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("uses").forGetter(Uses::uses),
                Codec.BOOL.fieldOf("must_consume").forGetter(Uses::mustConsume)
        ).apply(instance, Uses::new));

        public static final StreamCodec<ByteBuf, Uses> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Uses::uses,
                ByteBufCodecs.BOOL, Uses::mustConsume,
                Uses::new
        );

        public static final Uses UNRESTRICTED = new Uses(0, false);
    }


    public record Suffix(
            int num,
            @NonNull SuffixType type,
            int timerLifeTick
    ) {
        public static final Codec<Suffix> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("num").forGetter(Suffix::num),
                EnumCodecs.codec(SuffixType.class).fieldOf("type").forGetter(Suffix::type),
                Codec.INT.fieldOf("timer_life_tick").forGetter(Suffix::timerLifeTick)
        ).apply(instance, Suffix::new));

        public static final StreamCodec<ByteBuf, Suffix> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Suffix::num,
                EnumCodecs.streamCodec(SuffixType.class), Suffix::type,
                ByteBufCodecs.VAR_INT, Suffix::timerLifeTick,
                Suffix::new
        );
    }

    @Builder
    public record Modifications(
            float speedMultiplier,
            int recoil,
            float spread,
            float criticalChance
    ) {
        public static final Codec<Modifications> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("speed_multiplier").forGetter(Modifications::speedMultiplier),
                Codec.INT.fieldOf("recoil").forGetter(Modifications::recoil),
                Codec.FLOAT.fieldOf("spread").forGetter(Modifications::spread),
                Codec.FLOAT.fieldOf("critical_chance").forGetter(Modifications::criticalChance)
        ).apply(instance, Modifications::new));

        public static final StreamCodec<ByteBuf, Modifications> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, Modifications::speedMultiplier,
                ByteBufCodecs.VAR_INT, Modifications::recoil,
                ByteBufCodecs.FLOAT, Modifications::spread,
                ByteBufCodecs.FLOAT, Modifications::criticalChance,
                Modifications::new
        );

        public static ModificationsBuilder builder() {
            return new ModificationsBuilder()
                    .speedMultiplier(1)
                    .recoil(0)
                    .spread(0)
                    .criticalChance(0);
        }
    }

    /**
     *
     * @param piercing 可对同一实体重复伤害
     * @param penetrating 仅对同一实体造成一次伤害
     */
    @Builder
    public record Damage(
            float damage,
            @NonNull DamageType damageType,
            float explosion,
            float explosionRadius,
            boolean friendlyFire,
            boolean piercing,
            boolean penetrating,
            int projectileCount
    ) {
        public static final Codec<Damage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("damage").forGetter(Damage::damage),
                EnumCodecs.codec(DamageType.class).fieldOf("damage_type").forGetter(Damage::damageType),
                Codec.FLOAT.fieldOf("explosion").forGetter(Damage::explosion),
                Codec.FLOAT.fieldOf("explosion_radius").forGetter(Damage::explosionRadius),
                Codec.BOOL.fieldOf("friendly_fire").forGetter(Damage::friendlyFire),
                Codec.BOOL.fieldOf("piercing").forGetter(Damage::piercing),
                Codec.BOOL.fieldOf("penetrating").forGetter(Damage::penetrating),
                Codec.INT.fieldOf("projectile_count").forGetter(Damage::projectileCount)
        ).apply(instance, Damage::new));

        public static final StreamCodec<ByteBuf, Damage> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, Damage::damage,
                EnumCodecs.streamCodec(DamageType.class), Damage::damageType,
                ByteBufCodecs.FLOAT, Damage::explosion,
                ByteBufCodecs.FLOAT, Damage::explosionRadius,
                ByteBufCodecs.BOOL, Damage::friendlyFire,
                ByteBufCodecs.BOOL, Damage::piercing,
                ByteBufCodecs.BOOL, Damage::penetrating,
                ByteBufCodecs.VAR_INT, Damage::projectileCount,
                Damage::new
        );

        public static DamageBuilder builder() {
            return new DamageBuilder()
                    .damage(0)
                    .damageType(DamageType.Projectile)
                    .explosion(0)
                    .explosionRadius(0)
                    .friendlyFire(false)
                    .piercing(false)
                    .penetrating(false)
                    .projectileCount(1);
        }
    }

    @Builder
    public record Time(
            int lifeTick,
            int canHitShooterAfter
    ) {
        public static final Codec<Time> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("life_tick").forGetter(Time::lifeTick),
                Codec.INT.fieldOf("can_hit_shooter_after").forGetter(Time::canHitShooterAfter)
        ).apply(instance, Time::new));

        public static final StreamCodec<ByteBuf, Time> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Time::lifeTick,
                ByteBufCodecs.VAR_INT, Time::canHitShooterAfter,
                Time::new
        );

        public static TimeBuilder builder() {
            return new TimeBuilder()
                    .lifeTick(0)
                    .canHitShooterAfter(0);
        }
    }

    /**
     *
     * @param initialSpeed 初始化速度（米 / 刻）
     * @see <a href="https://tieba.baidu.com/p/9226674208?share=9105&fr=sharewise&see_lz=0&share_from=post&sfc=qqfriend&client_type=2&client_version=12.78.1.1&st=1785852329&is_video=false&unique=D2D580009B0D306C2D9848EB3725962D&source=12_16_sharecard_a">关于重力修正与 noita 运动学</a>
     */
    @Builder
    public record Motion(
            float initialSpeed,
            float spread,
            int gravity,
            float friction,
            float mass,
            float dieSpeedThreshold,
            int bounces
    ) {
        public static final Codec<Motion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("initial_speed").forGetter(Motion::initialSpeed),
                Codec.FLOAT.fieldOf("spread").forGetter(Motion::spread),
                Codec.INT.fieldOf("gravity").forGetter(Motion::gravity),
                Codec.FLOAT.fieldOf("friction").forGetter(Motion::friction),
                Codec.FLOAT.fieldOf("mass").forGetter(Motion::mass),
                Codec.FLOAT.fieldOf("die_speed_threshold").forGetter(Motion::dieSpeedThreshold),
                Codec.INT.fieldOf("bounces").forGetter(Motion::bounces)
        ).apply(instance, Motion::new));

        public static final StreamCodec<ByteBuf, Motion> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, Motion::initialSpeed,
                ByteBufCodecs.FLOAT, Motion::spread,
                ByteBufCodecs.VAR_INT, Motion::gravity,
                ByteBufCodecs.FLOAT, Motion::friction,
                ByteBufCodecs.FLOAT, Motion::mass,
                ByteBufCodecs.FLOAT, Motion::dieSpeedThreshold,
                ByteBufCodecs.VAR_INT, Motion::bounces,
                Motion::new
        );

        public static MotionBuilder builder() {
            return new MotionBuilder()
                    .initialSpeed(0)
                    .spread(0)
                    .gravity(0)
                    .friction(0)
                    .mass(0)
                    .dieSpeedThreshold(0)
                    .bounces(0);
        }
    }

    @Builder
    public record Other(
            int basePrice,
            int diggingStrength,
            float diggingPower
    ) {
        public static final Codec<Other> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("base_price").forGetter(Other::basePrice),
                Codec.INT.fieldOf("digging_strength").forGetter(Other::diggingStrength),
                Codec.FLOAT.fieldOf("digging_power").forGetter(Other::diggingPower)
        ).apply(instance, Other::new));

        public static final StreamCodec<ByteBuf, Other> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Other::basePrice,
                ByteBufCodecs.VAR_INT, Other::diggingStrength,
                ByteBufCodecs.FLOAT, Other::diggingPower,
                Other::new
        );

        public static OtherBuilder builder() {
            return new OtherBuilder()
                    .basePrice(0)
                    .diggingStrength(0)
                    .diggingPower(0);
        }
    }
}
