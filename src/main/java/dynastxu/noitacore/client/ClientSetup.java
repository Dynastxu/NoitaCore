package dynastxu.noitacore.client;

import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.client.gui.SpellClientTooltipComponent;
import dynastxu.noitacore.client.gui.SpellTooltipComponent;
import dynastxu.noitacore.client.gui.WandClientTooltipComponent;
import dynastxu.noitacore.client.gui.WandTooltipComponent;
import dynastxu.noitacore.client.model.FunkyModel;
import dynastxu.noitacore.client.model.LaserModel;
import dynastxu.noitacore.client.model.NukeModel;
import dynastxu.noitacore.client.renderer.*;
import dynastxu.noitacore.client.screen.WandScreen;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.common.wand.Caster;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.entity.EntityTypes;
import dynastxu.noitacore.item.Items;
import dynastxu.noitacore.item.SpellItem;
import dynastxu.noitacore.menu.MenuTypes;
import dynastxu.noitacore.menu.WandMenu;
import dynastxu.noitacore.particle.ParticleTypes;
import dynastxu.noitacore.particle.explosion.ExplosionParticleProvider;
import dynastxu.noitacore.particle.pixel.PixelParticleProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.IItemDecorator;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.registries.DeferredItem;
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
        event.registerEntityRenderer(EntityTypes.BLACK_HOLE.get(), BlackHoleRenderer::new);
        event.registerEntityRenderer(EntityTypes.BUCKSHOT.get(), BuckshotRenderer::new);
        event.registerEntityRenderer(EntityTypes.LASER_EMITTER.get(), LaserEmitterRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.@NonNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NukeModel.LAYER_LOCATION, NukeModel::createBodyLayer);
        event.registerLayerDefinition(FunkyModel.LAYER_LOCATION, FunkyModel::createBodyLayer);
        event.registerLayerDefinition(LaserModel.LAYER_LOCATION, LaserModel::createBodyLayer);
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

    @SubscribeEvent
    public static void registerItemDecorations(@NonNull RegisterItemDecorationsEvent event) {
        IItemDecorator spellDecorator = (guiGraphics, font, stack, xOffset, yOffset) -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            if (player == null) return false;
            if (!(stack.getItem() instanceof SpellItem)) return false;
            SpellData data = stack.get(DataComponents.SPELL_DATA);
            SpellAttributes attrs = stack.typeHolder().getData(DataMaps.SPELL_ATTRIBUTES);
            if (data == null || attrs == null) return false;
            if (Caster.canSkipConsumeUses(player, attrs)) return false;
            int maxUses = attrs.base().uses().uses();
            if (maxUses <= 0) return false;

            String text = String.valueOf(data.remainingUses());
            int color = 0xFFFFFFFF;
            if (data.remainingUses() <= 0) {
                color = 0xFFFF5555;
            } else if (data.remainingUses() < maxUses) {
                color = 0xFFFFFF55;
            }

            float textX = xOffset + 1;
            float textY = yOffset + 17 - font.lineHeight * 0.7f;
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(textX, textY);
            guiGraphics.pose().scale(0.7f, 0.7f);
            guiGraphics.text(font, text, 0, 0, color);
            guiGraphics.pose().popMatrix();
            return true;
        };

        for (DeferredItem<? extends SpellItem> spellItem : Items.SPELL_ITEMS) {
            event.register(spellItem.get(), spellDecorator);
        }
    }
}
