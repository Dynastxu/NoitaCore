package dynastxu.noitacore.menu;

import dynastxu.noitacore.common.wand.WandStatistics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(BuiltInRegistries.MENU, MODID);

    public static final List<Supplier<MenuType<WandMenu>>> WAND_MENUS;

    static {
        final List<Supplier<MenuType<WandMenu>>> wandMenus = new ArrayList<>();
        for (int i = 1; i <= WandStatistics.MAX_CAPACITY; i++) {
            final int containerSize = i;
            wandMenus.add(
                    MENU_TYPES.register("wand_menu_" + containerSize, () -> new MenuType<>(
                            (containerId, playerInventory) -> new WandMenu(containerId, playerInventory, containerSize),
                            FeatureFlags.DEFAULT_FLAGS)
                    )
            );
        }
        WAND_MENUS = Collections.unmodifiableList(wandMenus);
    }

    public static Supplier<MenuType<WandMenu>> getWandMenuType(int containerSize) {
        if (containerSize < 1 || containerSize > WandStatistics.MAX_CAPACITY) {
            throw new IllegalArgumentException("WandContainer size must be between 1 and " + WandStatistics.MAX_CAPACITY);
        }
        return WAND_MENUS.get(containerSize - 1);
    }
}
