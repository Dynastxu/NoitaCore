package dynastxu.noitacore.common.spell;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum SpellType {
    Projectile,
    Static,
    Passive,
    Utility,
    Modifier,
    Material,
    Multicast,
    Other;

    public static final Codec<SpellType> CODEC = Codec.STRING.xmap(SpellType::valueOf, Enum::name);

    public static final StreamCodec<ByteBuf, SpellType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Enum::name,
            SpellType::valueOf
    );
}
