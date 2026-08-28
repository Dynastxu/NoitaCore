package dynastxu.noitacore.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public final class Payloads {
    @SubscribeEvent
    public static void register(@NonNull RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                WandGuiOpenPayload.TYPE,
                WandGuiOpenPayload.STREAM_CODEC,
                WandGuiOpenPayload::handle
        );
        registrar.playToClient(
                NoitaBookOpenPayload.TYPE,
                NoitaBookOpenPayload.STREAM_CODEC,
                NoitaBookOpenPayload::handle
        );
    }
}
