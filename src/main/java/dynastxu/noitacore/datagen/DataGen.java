package dynastxu.noitacore.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public final class DataGen {
    @SubscribeEvent
    public static void onGatherClientData(GatherDataEvent.@NonNull Client event) {
        event.createProvider(ModLanguageProviderEnUS::new);
        event.createProvider(ModLanguageProviderZhCN::new);
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModDataMapProvider::new);
        event.createProvider(ModParticleDescriptionProvider::new);
    }

    @SubscribeEvent
    public static void onGatherServerData(GatherDataEvent.@NonNull Server event) {
        event.createProvider(ModDataMapProvider::new);
    }
}
