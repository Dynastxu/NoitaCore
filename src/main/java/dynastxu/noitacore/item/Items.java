package dynastxu.noitacore.item;

import dynastxu.noitacore.common.wand.WandStatistics;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.entity.EntityTypes;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Spells
    // Projectile
    public static final DeferredItem<SpellItem> SPELL_RUBBER_BALL = ITEMS.registerItem("spell_rubber_ball", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build()))
            .projectileType(EntityTypes.RUBBER_BALL.get()).build());

    public static final DeferredItem<SpellItem> SPELL_LIGHT_BULLET = ITEMS.registerItem("spell_light_bullet", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build()))
            .projectileType(EntityTypes.LIGHT_BULLET.get()).build());

    // Modifier
    public static final DeferredItem<SpellItem> SPELL_MANA_REDUCE = ITEMS.registerItem("spell_mana_reduce", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build())).build());

    // Multicast
    public static final DeferredItem<SpellItem> SPELL_BURST_2 = ITEMS.registerItem("spell_burst_2", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build())).build());

    public static final DeferredItem<SpellItem> SPELL_BURST_3 = ITEMS.registerItem("spell_burst_3", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build())).build());

    public static final DeferredItem<SpellItem> SPELL_BURST_4 = ITEMS.registerItem("spell_burst_4", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build())).build());

    public static final DeferredItem<SpellItem> SPELL_BURST_8 = ITEMS.registerItem("spell_burst_8", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build())).build());

    public static final DeferredItem<SpellItem> SPELL_BURST_X = ITEMS.registerItem("spell_burst_x", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(30).build())).build());

    // Wands
    public static final DeferredItem<WandItem> WAND_SMC_SC_NS = ITEMS.registerItem("wand_smc_sc_ns", properties -> new WandItem(
            properties.component(DataComponents.WAND_DATA.get(), new WandData(new WandStatistics(
                    false, 1, 2, 20, 2000, 20, 10, 0, new ArrayList<>(), 1
            )))));

    public static final DeferredItem<WandItem> WAND_LC_SC_S = ITEMS.registerItem("wand_lc_sc_s", properties -> new WandItem(
            properties.component(DataComponents.WAND_DATA.get(), new WandData(new WandStatistics(
                    true, 1, 2, 20, 2000, 20, 40, 0, new ArrayList<>(), 1
            )))));
}
