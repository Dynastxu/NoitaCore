package dynastxu.noitacore.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.common.wand.WandContainer;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;
import net.minecraft.core.NonNullList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record Spell(
        @NonNull SpellAttributes attributes,
        Integer index
) {
    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SpellAttributes.CODEC.fieldOf("base").forGetter(Spell::attributes),
            Codec.INT.fieldOf("index").forGetter(Spell::index)
    ).apply(instance, Spell::new));

    public static final StreamCodec<ByteBuf, Spell> STREAM_CODEC = StreamCodec.composite(
            SpellAttributes.STREAM_CODEC, Spell::attributes,
            ByteBufCodecs.VAR_INT, Spell::index,
            Spell::new
    );

    public Spell(SpellAttributes attributes) {
        this(attributes, null);
    }

    public boolean isFromAlwaysCasts() {
        return index == null;
    }

    public SpellData getDataFrom(@org.jspecify.annotations.NonNull NonNullList<ItemStack> inventory) {
        return inventory.get(index).get(DataComponents.SPELL_DATA);
    }

    public void consume(@org.jspecify.annotations.NonNull NonNullList<ItemStack> inventory) {
        ItemStack itemStack = inventory.get(index);
        SpellData data = itemStack.get(DataComponents.SPELL_DATA);
        if (data != null) {
            int remainingUses = data.remainingUses();
            remainingUses--;
            if (remainingUses < 0) {
                remainingUses = 0;
            }
            itemStack.set(DataComponents.SPELL_DATA, data.toBuilder().remainingUses(remainingUses).build());
        }
    }
}
