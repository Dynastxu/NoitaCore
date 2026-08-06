package dynastxu.noitacore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public final class DataGen {
    @SubscribeEvent
    public static void onGatherClientData(GatherDataEvent.@NonNull Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new ModLanguageProviderEnUS(packOutput));
        generator.addProvider(true, new ModLanguageProviderZhCN(packOutput));
        generator.addProvider(true, new ModModelProvider(packOutput));
    }

    @SubscribeEvent
    public static void onGatherServerData(GatherDataEvent.@NonNull Server event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new ModDataMapProvider(packOutput, lookupProvider));
    }
}
