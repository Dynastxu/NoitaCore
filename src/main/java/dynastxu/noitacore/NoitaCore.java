package dynastxu.noitacore;

import com.mojang.logging.LogUtils;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(NoitaCore.MODID)
public final class NoitaCore {
    public static final String MODID = "noitacore";
    private static final Logger LOGGER = LogUtils.getLogger();

    public NoitaCore(IEventBus modEventBus, ModContainer modContainer) {
        Items.ITEMS.register(modEventBus);
        DataComponents.COMPONENT_TYPES.register(modEventBus);
        CreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        MenuTypes.MENU_TYPES.register(modEventBus);
    }
}
