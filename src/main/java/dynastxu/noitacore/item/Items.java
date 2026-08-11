package dynastxu.noitacore.item;

import dynastxu.noitacore.common.wand.WandStatistics;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Spells
    // Projectile
    public static final DeferredItem<Item> SPELL_RUBBER_BALL = ITEMS.registerItem("spell_rubber_ball", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build()))
            .projectileType(EntityTypes.RUBBER_BALL.get()).build());

    public static final DeferredItem<Item> SPELL_LIGHT_BULLET = ITEMS.registerItem("spell_light_bullet", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build()))
            .projectileType(EntityTypes.LIGHT_BULLET.get()).build());

    // Multicast
    public static final DeferredItem<Item> SPELL_BURST_2 = ITEMS.registerItem("spell_burst_2", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build())).build());

    // Wands
    public static final DeferredItem<Item> WAND_SMC_SC_NS = ITEMS.registerItem("wand_smc_sc_ns", properties -> new WandItem(
            properties.component(DataComponents.WAND_DATA.get(), new WandData(new WandStatistics(
                    false, 1, 2, 20, 2000, 200, 10, 0, new ArrayList<>(), 1
            )))));

    public static final DeferredItem<Item> WAND_LC_SC_S = ITEMS.registerItem("wand_lc_sc_s", properties -> new WandItem(
            properties.component(DataComponents.WAND_DATA.get(), new WandData(new WandStatistics(
                    true, 1, 2, 20, 2000, 200, 40, 0, new ArrayList<>(), 1
            )))));
}
