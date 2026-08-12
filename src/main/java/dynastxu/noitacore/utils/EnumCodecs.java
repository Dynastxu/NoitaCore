package dynastxu.noitacore.utils;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public interface EnumCodecs {
    static <E extends Enum<E>> Codec<E> codec(Class<E> enumClass) {
        return Codec.STRING.xmap(
                name -> Enum.valueOf(enumClass, name),
                Enum::name
        );
    }

    @Contract(value = "_ -> new", pure = true)
    static <E extends Enum<E>> @NonNull StreamCodec<ByteBuf, E> streamCodec(Class<E> enumClass) {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                Enum::name,
                name -> Enum.valueOf(enumClass, name)
        );
    }
}
