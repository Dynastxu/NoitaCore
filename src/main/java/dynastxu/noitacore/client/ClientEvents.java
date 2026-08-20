package dynastxu.noitacore.client;

import com.mojang.datafixers.util.Either;
import dynastxu.noitacore.client.gui.MaterialStatsTooltipComponent;
import dynastxu.noitacore.client.gui.SpellTooltipComponent;
import dynastxu.noitacore.client.gui.WandTooltipComponent;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.datamap.DataMaps;
import dynastxu.noitacore.datamap.MaterialStats;
import dynastxu.noitacore.datamap.SpellAttributes;
import dynastxu.noitacore.item.SpellItem;
import dynastxu.noitacore.item.WandItem;
import dynastxu.noitacore.network.WandGuiOpenPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public final class ClientEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (KeyMappings.OPEN_WAND_GUI.get().consumeClick()) {
            if (KeyMappings.OPEN_WAND_GUI.get().isDown()) {
                ClientPacketDistributor.sendToServer(new WandGuiOpenPayload());
            }
        }
    }

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.@NonNull GatherComponents event) {
        ItemStack stack = event.getItemStack();
        Minecraft mc = Minecraft.getInstance();

        if (stack.getItem() instanceof WandItem) {
            WandData wandData = stack.get(DataComponents.WAND_DATA);
            if (wandData == null) return;
            event.getTooltipElements().add(Either.right(new WandTooltipComponent(wandData)));
        } else if (stack.getItem() instanceof SpellItem) {
            SpellAttributes attrs = stack.getData(DataMaps.SPELL_ATTRIBUTES);
            if (attrs == null) return;
            SpellData spellData = stack.get(DataComponents.SPELL_DATA);
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            event.getTooltipElements().add(Either.right(new SpellTooltipComponent(attrs, spellData, player)));
        }

        if (mc.options.advancedItemTooltips) {
            Block block = Block.byItem(stack.getItem());
            if (block != Blocks.AIR) {
                MaterialStats stats = BuiltInRegistries.BLOCK.wrapAsHolder(block).getData(DataMaps.MATERIAL_STATS);
                if (stats != null) {
                    event.getTooltipElements().add(Either.right(new MaterialStatsTooltipComponent(stats)));
                }
            }
        }
    }
}
