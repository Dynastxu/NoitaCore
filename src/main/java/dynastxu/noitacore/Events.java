package dynastxu.noitacore;

import dynastxu.noitacore.attachment.UnclockedSpells;
import dynastxu.noitacore.item.SpellItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public class Events {
    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.@NonNull Post event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemEntity().getItem();
        Item item = itemStack.getItem();

        if (item instanceof SpellItem) {
            UnclockedSpells.unlock(player, BuiltInRegistries.ITEM.wrapAsHolder(item));
        }
    }
}
