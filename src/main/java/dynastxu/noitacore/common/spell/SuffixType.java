package dynastxu.noitacore.common.spell;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum SuffixType {
    Timer,
    Trigger;

    public static final Codec<SuffixType> CODEC = Codec.STRING.xmap(SuffixType::valueOf, Enum::name);

    public static final StreamCodec<ByteBuf, SuffixType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Enum::name,
            SuffixType::valueOf
    );
}
