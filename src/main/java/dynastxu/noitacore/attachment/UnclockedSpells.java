package dynastxu.noitacore.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record UnclockedSpells (
        List<Holder<Item>> spells
) {
    public static final Codec<UnclockedSpells> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Item.CODEC).fieldOf("spells").forGetter(UnclockedSpells::spells)
            ).apply(instance, UnclockedSpells::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, UnclockedSpells> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.<RegistryFriendlyByteBuf, Holder<Item>>list().apply(Item.STREAM_CODEC), UnclockedSpells::spells,
            UnclockedSpells::new
    );

    public UnclockedSpells() {
        this(List.of());
    }

    private UnclockedSpells unlock(Holder<Item> spell) {
        spells.add(spell);
        return this;
    }

    public static void unlock(@NonNull Player player, Holder<Item> spell) {
        player.setData(Attachments.UNLOCKED_SPELLS, player.getData(Attachments.UNLOCKED_SPELLS).unlock(spell));
    }
}
