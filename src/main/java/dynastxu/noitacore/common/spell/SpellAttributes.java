package dynastxu.noitacore.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.NonNull;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * @see <a href="https://noita.wiki.gg/wiki/Spells">wiki</a>
 */
@Builder
public record SpellAttributes(
        BaseAttributes base,
        Modifications modifications,
        Damage damage,
        Time time,
        Suffix suffix,
        Motion motion,
        Other other
) {
    public static final Codec<SpellAttributes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BaseAttributes.CODEC.fieldOf("base").forGetter(SpellAttributes::base),
            Modifications.CODEC.fieldOf("modifications").forGetter(SpellAttributes::modifications),
            Damage.CODEC.fieldOf("damage").forGetter(SpellAttributes::damage),
            Time.CODEC.fieldOf("time").forGetter(SpellAttributes::time),
            Suffix.CODEC.fieldOf("suffix").forGetter(SpellAttributes::suffix),
            Motion.CODEC.fieldOf("motion").forGetter(SpellAttributes::motion),
            Other.CODEC.fieldOf("other").forGetter(SpellAttributes::other)
    ).apply(instance, SpellAttributes::new));

    public static final StreamCodec<ByteBuf, SpellAttributes> STREAM_CODEC = StreamCodec.composite(
            BaseAttributes.STREAM_CODEC, SpellAttributes::base,
            Modifications.STREAM_CODEC, SpellAttributes::modifications,
            Damage.STREAM_CODEC, SpellAttributes::damage,
            Time.STREAM_CODEC, SpellAttributes::time,
            Suffix.STREAM_CODEC, SpellAttributes::suffix,
            Motion.STREAM_CODEC, SpellAttributes::motion,
            Other.STREAM_CODEC, SpellAttributes::other,
            SpellAttributes::new
    );

    public boolean isModifier() {
        return base.type == SpellType.Modifier;
    }

    public boolean isMulticast() {
        return base.type == SpellType.Multicast;
    }

    @Builder
    public record BaseAttributes(
            @NonNull SpellType type,
            Uses uses,
            int manaDrain,
            int castDelayTick,
            int rechargeTick,
            int draws
    ) {
        public static final Codec<BaseAttributes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SpellType.CODEC.fieldOf("type").forGetter(BaseAttributes::type),
                Uses.CODEC.fieldOf("uses").forGetter(BaseAttributes::uses),
                Codec.INT.fieldOf("mana_drain").forGetter(BaseAttributes::manaDrain),
                Codec.INT.fieldOf("cast_delay_tick").forGetter(BaseAttributes::castDelayTick),
                Codec.INT.fieldOf("recharge_tick").forGetter(BaseAttributes::rechargeTick),
                Codec.INT.fieldOf("draws").forGetter(BaseAttributes::draws)
        ).apply(instance, BaseAttributes::new));

        public static final StreamCodec<ByteBuf, BaseAttributes> STREAM_CODEC = StreamCodec.composite(
                SpellType.STREAM_CODEC, BaseAttributes::type,
                Uses.STREAM_CODEC, BaseAttributes::uses,
                ByteBufCodecs.VAR_INT, BaseAttributes::manaDrain,
                ByteBufCodecs.VAR_INT, BaseAttributes::castDelayTick,
                ByteBufCodecs.VAR_INT, BaseAttributes::rechargeTick,
                ByteBufCodecs.VAR_INT, BaseAttributes::draws,
                BaseAttributes::new
        );
    }

    @Builder
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
    }


    @Builder
    public record Suffix(
            int num,
            SuffixType triggerType,
            int timerLifeTick
    ) {
        public static final Codec<Suffix> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("num").forGetter(Suffix::num),
                SuffixType.CODEC.fieldOf("trigger_type").forGetter(Suffix::triggerType),
                Codec.INT.fieldOf("timer_life_tick").forGetter(Suffix::timerLifeTick)
        ).apply(instance, Suffix::new));

        public static final StreamCodec<ByteBuf, Suffix> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Suffix::num,
                SuffixType.STREAM_CODEC, Suffix::triggerType,
                ByteBufCodecs.VAR_INT, Suffix::timerLifeTick,
                Suffix::new
        );
    }

    @Builder
    public record Modifications(
            float speedMultiplier,
            float recoil,
            float spread
    ) {
        public static final Codec<Modifications> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("speed_multiplier").forGetter(Modifications::speedMultiplier),
                Codec.FLOAT.fieldOf("recoil").forGetter(Modifications::recoil),
                Codec.FLOAT.fieldOf("spread").forGetter(Modifications::spread)
        ).apply(instance, Modifications::new));

        public static final StreamCodec<ByteBuf, Modifications> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, Modifications::speedMultiplier,
                ByteBufCodecs.FLOAT, Modifications::recoil,
                ByteBufCodecs.FLOAT, Modifications::spread,
                Modifications::new
        );
    }

    @Builder
    public record Damage(
            float projectile,
            float explosion,
            float explosionRadius,
            boolean friendlyFire
    ) {
        public static final Codec<Damage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("projectile").forGetter(Damage::projectile),
                Codec.FLOAT.fieldOf("explosion").forGetter(Damage::explosion),
                Codec.FLOAT.fieldOf("explosion_radius").forGetter(Damage::explosionRadius),
                Codec.BOOL.fieldOf("friendly_fire").forGetter(Damage::friendlyFire)
        ).apply(instance, Damage::new));

        public static final StreamCodec<ByteBuf, Damage> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, Damage::projectile,
                ByteBufCodecs.FLOAT, Damage::explosion,
                ByteBufCodecs.FLOAT, Damage::explosionRadius,
                ByteBufCodecs.BOOL, Damage::friendlyFire,
                Damage::new
        );
    }

    @Builder
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
    }

    @Builder
    public record Other(
            int basePrice
    ) {
        public static final Codec<Other> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("base_price").forGetter(Other::basePrice)
        ).apply(instance, Other::new));

        public static final StreamCodec<ByteBuf, Other> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Other::basePrice,
                Other::new
        );
    }
}
