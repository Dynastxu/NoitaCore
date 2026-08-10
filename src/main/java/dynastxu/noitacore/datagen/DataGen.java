package dynastxu.noitacore.datagen;

import dynastxu.noitacore.DamageTypes;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
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

        event.createDatapackRegistryObjects(new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, bootstrap -> {
                    bootstrap.register(DamageTypes.SPELL_PROJECTILE, new DamageType("spell_projectile",
                            DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                            0f,
                            DamageEffects.HURT,
                            DeathMessageType.DEFAULT));
                }));
    }

    @SubscribeEvent
    public static void onGatherServerData(GatherDataEvent.@NonNull Server event) {
        event.createProvider(ModDataMapProvider::new);
    }
}
