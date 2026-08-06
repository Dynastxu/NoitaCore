package dynastxu.noitacore.client;

import dynastxu.noitacore.client.renderer.RubberBallRenderer;
import dynastxu.noitacore.entity.EntityTypes;
import dynastxu.noitacore.menu.MenuTypes;
import dynastxu.noitacore.menu.WandMenu;
import dynastxu.noitacore.particle.ParticleTypes;
import dynastxu.noitacore.particle.PixelParticleProvider;
import dynastxu.noitacore.screen.WandScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public final class ClientSetup {
    @SuppressWarnings("RedundantTypeArguments")
    @SubscribeEvent
    public static void registerMenuScreens(@NonNull RegisterMenuScreensEvent event) {
        MenuTypes.WAND_MENUS.forEach(menuType -> event.<WandMenu, WandScreen>register(menuType.get(), WandScreen::new));
    }

    @SubscribeEvent
    public static void registerBindings(@NonNull RegisterKeyMappingsEvent event) {
        event.register(KeyMappings.OPEN_WAND_GUI.get());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.@NonNull RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypes.RUBBER_BALL.get(), RubberBallRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(@NonNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypes.PIXEL_PARTICLE.get(), PixelParticleProvider::new);
    }
}
