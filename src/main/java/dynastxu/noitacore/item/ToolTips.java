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

//        if (event.getFlags().isAdvanced()) {
//            Block block = Block.byItem(stack.getItem());
//            if (block != Blocks.AIR) {
//                MaterialStats stats = BuiltInRegistries.BLOCK.wrapAsHolder(block).getData(DataMaps.MATERIAL_STATS);
//                if (stats != null) {
//                    tooltip.add(Component.translatable("tooltip.noitacore.durability")
//                            .append(":").append(String.valueOf(stats.durability())).withStyle(ChatFormatting.GRAY));
//                    tooltip.add(Component.translatable("tooltip.noitacore.density")
//                            .append(":").append(String.valueOf(stats.density())).withStyle(ChatFormatting.GRAY));
//                    tooltip.add(Component.translatable("tooltip.noitacore.hardness")
//                            .append(":").append(String.valueOf(stats.hardness())).withStyle(ChatFormatting.GRAY));
//                    tooltip.add(Component.translatable("tooltip.noitacore.conductive")
//                            .append(":").append(stats.conductive() ? Component.translatable("gui.yes") : Component.translatable("gui.no")).withStyle(ChatFormatting.GRAY));
//                }
//            }
//        }
    }
}
