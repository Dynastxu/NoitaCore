package dynastxu.noitacore.client;

import dynastxu.noitacore.client.model.NukeModel;
import dynastxu.noitacore.client.renderer.EmptyRenderer;
import dynastxu.noitacore.client.renderer.NukeRenderer;
import dynastxu.noitacore.client.renderer.RubberBallRenderer;
import dynastxu.noitacore.client.screen.WandScreen;
import dynastxu.noitacore.entity.EntityTypes;
import dynastxu.noitacore.menu.MenuTypes;
import dynastxu.noitacore.menu.WandMenu;
import dynastxu.noitacore.particle.ParticleTypes;
import dynastxu.noitacore.particle.explosion.ExplosionParticleProvider;
import dynastxu.noitacore.particle.pixel.PixelParticleProvider;
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
        event.registerEntityRenderer(EntityTypes.LIGHT_BULLET.get(), EmptyRenderer::new);
        event.registerEntityRenderer(EntityTypes.NUKE.get(), NukeRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.@NonNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NukeModel.LAYER_LOCATION, NukeModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerParticles(@NonNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypes.PIXEL_PARTICLE.get(), PixelParticleProvider::new);
        event.registerSpriteSet(ParticleTypes.EXPLOSION_PARTICLE.get(), ExplosionParticleProvider::new);
    }
}
