package dynastxu.noitacore.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.item.Items;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record UnlockedSpells(
        List<Holder<Item>> spells
) {
    public static final MapCodec<UnlockedSpells> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.list(Item.CODEC).fieldOf("spells").forGetter(UnlockedSpells::spells)
            ).apply(instance, UnlockedSpells::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, UnlockedSpells> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.<RegistryFriendlyByteBuf, Holder<Item>>list().apply(Item.STREAM_CODEC), UnlockedSpells::spells,
            UnlockedSpells::new
    );

    public UnlockedSpells() {
        this(List.of());
    }

    public static void unlock(@NonNull Player player, Holder<Item> spell) {
        UnlockedSpells data = player.getData(Attachments.UNLOCKED_SPELLS);
        if (!data.isUnlocked(spell)) {
            player.setData(Attachments.UNLOCKED_SPELLS, data.unlock(spell));
        }
    }

    public static void unlockAll(@NonNull Player player) {
        List<Holder<Item>> allSpells = Items.SPELL_ITEMS.stream()
                .map(DeferredItem::getDelegate)
                .toList();
        player.setData(Attachments.UNLOCKED_SPELLS, new UnlockedSpells(allSpells));
    }

    public static void lock(@NonNull Player player, Holder<Item> spell) {
        UnlockedSpells data = player.getData(Attachments.UNLOCKED_SPELLS);
        if (data.isUnlocked(spell)) {
            player.setData(Attachments.UNLOCKED_SPELLS, data.lock(spell));
        }
    }

    public static void lockAll(@NonNull Player player) {
        player.setData(Attachments.UNLOCKED_SPELLS, new UnlockedSpells());
    }

    private boolean isUnlocked(Holder<Item> spell) {
        return spells.contains(spell);
    }

    private @NonNull UnlockedSpells unlock(Holder<Item> spell) {
        if (!isUnlocked(spell)) {
            List<Holder<Item>> spells = new ArrayList<>(this.spells);
            spells.add(spell);
            return new UnlockedSpells(spells);
        } else {
            return this;
        }
    }

    private @NonNull UnlockedSpells lock(Holder<Item> spell) {
        if (isUnlocked(spell)) {
            List<Holder<Item>> spells = new ArrayList<>(this.spells);
            spells.remove(spell);
            return new UnlockedSpells(spells);
        } else {
            return this;
        }
    }

    public float unlockProgress() {
        return (float) spells.size() / Items.SPELL_ITEMS.size();
    }
}
