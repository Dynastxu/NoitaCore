package dynastxu.noitacore.client;

import dynastxu.noitacore.network.WandGuiOpenPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

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
}
