package dynastxu.noitacore.common.wand;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.common.spell.SpellAttributes;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record WandStatistics(
        boolean shuffle,
        int spellsPerCast,
        int castDelayTick,
        int rechargeTick,
        int manaMax,
        int manaChargeSpeed,
        int capacity,
        float spread,
        @NonNull List<SpellAttributes> alwaysCasts,
        float speedMultiplier
) {
    public static final int MAX_CAPACITY = 66;
    public static final Codec<WandStatistics> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.BOOL.fieldOf("shuffle").forGetter(WandStatistics::shuffle),
                            Codec.INT.fieldOf("spells_per_cast").forGetter(WandStatistics::spellsPerCast),
                            Codec.INT.fieldOf("cast_delay_tick").forGetter(WandStatistics::castDelayTick),
                            Codec.INT.fieldOf("recharge_tick").forGetter(WandStatistics::rechargeTick),
                            Codec.INT.fieldOf("mana_max").forGetter(WandStatistics::manaMax),
                            Codec.INT.fieldOf("mana_charge_speed").forGetter(WandStatistics::manaChargeSpeed),
                            Codec.INT.fieldOf("capacity").forGetter(WandStatistics::capacity),
                            Codec.FLOAT.fieldOf("spread").forGetter(WandStatistics::spread),
                            Codec.list(SpellAttributes.CODEC).fieldOf("always_casts").forGetter(WandStatistics::alwaysCasts),
                            Codec.FLOAT.fieldOf("speed_multiplier").forGetter(WandStatistics::speedMultiplier)
                    )
                    .apply(instance, WandStatistics::new)
    );

    public static final StreamCodec<ByteBuf, WandStatistics> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, WandStatistics::shuffle,
            ByteBufCodecs.VAR_INT, WandStatistics::spellsPerCast,
            ByteBufCodecs.VAR_INT, WandStatistics::castDelayTick,
            ByteBufCodecs.VAR_INT, WandStatistics::rechargeTick,
            ByteBufCodecs.VAR_INT, WandStatistics::manaMax,
            ByteBufCodecs.VAR_INT, WandStatistics::manaChargeSpeed,
            ByteBufCodecs.VAR_INT, WandStatistics::capacity,
            ByteBufCodecs.FLOAT, WandStatistics::spread,
            ByteBufCodecs.<ByteBuf, SpellAttributes>list().apply(SpellAttributes.STREAM_CODEC), WandStatistics::alwaysCasts,
            ByteBufCodecs.FLOAT, WandStatistics::speedMultiplier,
            WandStatistics::new
    );
}
