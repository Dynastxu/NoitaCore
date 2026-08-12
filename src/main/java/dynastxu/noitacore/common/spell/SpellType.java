package dynastxu.noitacore.common.spell;

import com.mojang.serialization.Codec;
import dynastxu.noitacore.datagen.ITranslatableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public enum SpellType implements ITranslatableEnum {
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

    @Override
    public @NonNull String getTranslationKey() {
        return "enum." + MODID + ".spell_type." + this.name();
    }
}
