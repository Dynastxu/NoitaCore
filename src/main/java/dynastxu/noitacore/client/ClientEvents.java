package dynastxu.noitacore.client;

import com.mojang.datafixers.util.Either;
import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.client.gui.SpellTooltipComponent;
import dynastxu.noitacore.client.gui.WandTooltipComponent;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.item.SpellItem;
import dynastxu.noitacore.item.WandItem;
import dynastxu.noitacore.network.WandGuiOpenPayload;
import net.minecraft.world.item.ItemStack;
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

        if (stack.getItem() instanceof WandItem) {
            WandData wandData = stack.get(DataComponents.WAND_DATA);
            if (wandData == null) return;
            event.getTooltipElements().add(Either.right(new WandTooltipComponent(wandData)));
        } else if (stack.getItem() instanceof SpellItem) {
            SpellAttributes attrs = stack.getData(DataMaps.SPELL_ATTRIBUTES);
            if (attrs == null) return;
            SpellData spellData = stack.get(DataComponents.SPELL_DATA);
            event.getTooltipElements().add(Either.right(new SpellTooltipComponent(attrs, spellData)));
        }
    }
}
