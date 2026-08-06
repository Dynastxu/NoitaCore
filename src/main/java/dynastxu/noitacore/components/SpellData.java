package dynastxu.noitacore.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 *
 * @param remainingUses 剩余施放次数
 */
@Builder(toBuilder = true)
public record SpellData(
        int remainingUses
) {
    public static final Codec<SpellData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("remaining_uses").forGetter(SpellData::remainingUses)
                    )
                    .apply(instance, SpellData::new)
    );

    public static final StreamCodec<ByteBuf, SpellData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SpellData::remainingUses,
            SpellData::new
    );

    public SpellData() {
        this(0);
    }
}
