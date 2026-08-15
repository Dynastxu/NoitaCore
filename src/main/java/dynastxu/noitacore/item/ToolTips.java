package dynastxu.noitacore.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public class ToolTips {
    @SubscribeEvent
    public static void addTooltips(@NonNull ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        // Spells
        if (stack.getItem() instanceof SpellItem spellItem) {
            tooltip.add(Component.translatable(spellItem.getDescriptionId() + ".description").withStyle(ChatFormatting.BLUE));
        }
    }
}
