package dynastxu.noitacore.common.spell;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public record Spell(
        @lombok.NonNull Holder<Item> itemHolder,
        int index,
        boolean isAlwaysCast
) {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UnitSpellChain.ITEM_HOLDER_CODEC.fieldOf("base").forGetter(Spell::itemHolder),
            Codec.INT.fieldOf("index").forGetter(Spell::index),
            Codec.BOOL.fieldOf("isAlwaysCast").forGetter(Spell::isAlwaysCast)
    ).apply(instance, Spell::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, Spell::itemHolder,
            ByteBufCodecs.VAR_INT, Spell::index,
            ByteBufCodecs.BOOL, Spell::isAlwaysCast,
            Spell::new
    );

    public @Nullable SpellData getDataFrom(@NonNull NonNullList<ItemStack> inventory) {
        if (isAlwaysCast) {
            throw new IllegalArgumentException("始终施放法术不应尝试获取数据，索引：" + index);
        }
        return inventory.get(index).get(DataComponents.SPELL_DATA);
    }

    public void consume(@NonNull NonNullList<ItemStack> inventory) {
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

    public @Nullable SpellAttributes getAttributes() {
        return itemHolder.getData(DataMaps.SPELL_ATTRIBUTES);
    }

    public @NonNull String getName() {
        return itemHolder.value().getName(itemHolder.value().getDefaultInstance()).getString();
    }
}
