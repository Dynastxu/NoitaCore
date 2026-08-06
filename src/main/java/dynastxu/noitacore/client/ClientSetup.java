package dynastxu.noitacore.client;

import dynastxu.noitacore.MenuTypes;
import dynastxu.noitacore.menu.WandMenu;
import dynastxu.noitacore.screen.WandScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
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
}
