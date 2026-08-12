package dynastxu.noitacore.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
@Builder
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

    public record Modifications(
            float speedMultiplier,
            float recoil,
            float spread,
            float criticalChance
    ) {
        public static final Codec<Modifications> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("speed_multiplier").forGetter(Modifications::speedMultiplier),
                Codec.FLOAT.fieldOf("recoil").forGetter(Modifications::recoil),
                Codec.FLOAT.fieldOf("spread").forGetter(Modifications::spread),
                Codec.FLOAT.fieldOf("critical_chance").forGetter(Modifications::criticalChance)
        ).apply(instance, Modifications::new));

        public static final StreamCodec<ByteBuf, Modifications> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, Modifications::speedMultiplier,
                ByteBufCodecs.FLOAT, Modifications::recoil,
                ByteBufCodecs.FLOAT, Modifications::spread,
                ByteBufCodecs.FLOAT, Modifications::criticalChance,
                Modifications::new
        );
    }

    public record Damage(
            float damage,
            @NonNull DamageType damageType,
            float explosion,
            float explosionRadius,
            boolean friendlyFire,
            boolean piercing,
            boolean penetrating
    ) {
        public static final Codec<Damage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("damage").forGetter(Damage::damage),
                EnumCodecs.codec(DamageType.class).fieldOf("damage_type").forGetter(Damage::damageType),
                Codec.FLOAT.fieldOf("explosion").forGetter(Damage::explosion),
                Codec.FLOAT.fieldOf("explosion_radius").forGetter(Damage::explosionRadius),
                Codec.BOOL.fieldOf("friendly_fire").forGetter(Damage::friendlyFire),
                Codec.BOOL.fieldOf("piercing").forGetter(Damage::piercing),
                Codec.BOOL.fieldOf("penetrating").forGetter(Damage::penetrating)
        ).apply(instance, Damage::new));

        public static final StreamCodec<ByteBuf, Damage> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, Damage::damage,
                EnumCodecs.streamCodec(DamageType.class), Damage::damageType,
                ByteBufCodecs.FLOAT, Damage::explosion,
                ByteBufCodecs.FLOAT, Damage::explosionRadius,
                ByteBufCodecs.BOOL, Damage::friendlyFire,
                ByteBufCodecs.BOOL, Damage::piercing,
                ByteBufCodecs.BOOL, Damage::penetrating,
                Damage::new
        );
    }

    public record Time(
            int lifeTick
    ) {
        public static final Codec<Time> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("life_tick").forGetter(Time::lifeTick)
        ).apply(instance, Time::new));

        public static final StreamCodec<ByteBuf, Time> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Time::lifeTick,
                Time::new
        );
    }

    /**
     *
     * @param initialSpeed 初始化速度（米 / 刻）
     * @see <a href="https://tieba.baidu.com/p/9226674208?share=9105&fr=sharewise&see_lz=0&share_from=post&sfc=qqfriend&client_type=2&client_version=12.78.1.1&st=1785852329&is_video=false&unique=D2D580009B0D306C2D9848EB3725962D&source=12_16_sharecard_a">关于重力修正与 noita 运动学</a>
     */
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
    }
}
