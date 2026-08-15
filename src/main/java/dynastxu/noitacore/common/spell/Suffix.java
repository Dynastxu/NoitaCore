package dynastxu.noitacore.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.utils.EnumCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record Suffix(
        SuffixType type,
        UnitSpellChain chain
) {
    public static final Codec<Suffix> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EnumCodecs.codec(SuffixType.class).fieldOf("type").forGetter(Suffix::type),
            UnitSpellChain.CODEC.fieldOf("chain").forGetter(Suffix::chain)
    ).apply(instance, Suffix::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Suffix> STREAM_CODEC = StreamCodec.composite(
            EnumCodecs.streamCodec(SuffixType.class), Suffix::type,
            UnitSpellChain.STREAM_CODEC, Suffix::chain,
            Suffix::new
    );
}
