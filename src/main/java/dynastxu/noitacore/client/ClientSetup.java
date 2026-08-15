package dynastxu.noitacore.client;

import dynastxu.noitacore.client.gui.SpellClientTooltipComponent;
import dynastxu.noitacore.client.gui.SpellTooltipComponent;
import dynastxu.noitacore.client.gui.WandClientTooltipComponent;
import dynastxu.noitacore.client.gui.WandTooltipComponent;
import dynastxu.noitacore.client.model.FunkyModel;
import dynastxu.noitacore.client.model.NukeModel;
import dynastxu.noitacore.client.renderer.EmptyRenderer;
import dynastxu.noitacore.client.renderer.FunkyRenderer;
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
import net.neoforged.neoforge.client.event.*;
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
        event.registerEntityRenderer(EntityTypes.CRUMBLING_EARTH.get(), EmptyRenderer::new);
        event.registerEntityRenderer(EntityTypes.FUNKY.get(), FunkyRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.@NonNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NukeModel.LAYER_LOCATION, NukeModel::createBodyLayer);
        event.registerLayerDefinition(FunkyModel.LAYER_LOCATION, FunkyModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerParticles(@NonNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypes.PIXEL_PARTICLE.get(), PixelParticleProvider::new);
        event.registerSpriteSet(ParticleTypes.EXPLOSION_PARTICLE.get(), ExplosionParticleProvider::new);
    }

    @SubscribeEvent
    public static void registerTooltipFactories(@NonNull RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(WandTooltipComponent.class, WandClientTooltipComponent::new);
        event.register(SpellTooltipComponent.class, SpellClientTooltipComponent::new);
    }
}
