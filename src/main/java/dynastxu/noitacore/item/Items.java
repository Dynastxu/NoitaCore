package dynastxu.noitacore.item;

import dynastxu.noitacore.common.wand.WandStatistics;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.entity.EntityTypes;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

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

    public static final DeferredItem<SpellItem> SPELL_LIGHT_BULLET_TRIGGER = ITEMS.registerItem("spell_light_bullet_trigger", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build()))
            .projectileType(EntityTypes.LIGHT_BULLET.get()).build());

    public static final DeferredItem<SpellItem> SPELL_LIGHT_BULLET_TRIGGER_2 = ITEMS.registerItem("spell_light_bullet_trigger_2", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build()))
            .projectileType(EntityTypes.LIGHT_BULLET.get()).build());

    public static final DeferredItem<SpellItem> SPELL_LIGHT_BULLET_TIMER = ITEMS.registerItem("spell_light_bullet_timer", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build()))
            .projectileType(EntityTypes.LIGHT_BULLET.get()).build());

    public static final DeferredItem<SpellItem> SPELL_NUKE = ITEMS.registerItem("spell_nuke", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(1).build()))
            .projectileType(EntityTypes.NUKE.get()).build());

    public static final DeferredItem<SpellItem> SPELL_NUKE_GIGA = ITEMS.registerItem("spell_nuke_giga", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(1).build()))
            .projectileType(EntityTypes.NUKE.get()).build());

    public static final DeferredItem<SpellItem> SPELL_CRUMBLING_EARTH = ITEMS.registerItem("spell_crumbling_earth", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(3).build()))
            .projectileType(EntityTypes.CRUMBLING_EARTH.get()).build());

    public static final DeferredItem<SpellItem> SPELL_FUNKY = ITEMS.registerItem("spell_funky", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build()))
            .projectileType(EntityTypes.FUNKY.get()).build());

    // Modifier
    public static final DeferredItem<SpellItem> SPELL_MANA_REDUCE = ITEMS.registerItem("spell_mana_reduce", properties -> SpellItem.builder()
            .properties(properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(0).build())).build());

    public static final DeferredItem<SpellItem> SPELL_CRITICAL_HIT = ITEMS.registerItem("spell_critical_hit", properties -> SpellItem.builder()
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

    public static final List<DeferredItem<SpellItem>> SPELL_ITEMS = List.of(
            SPELL_RUBBER_BALL,
            SPELL_LIGHT_BULLET,
            SPELL_LIGHT_BULLET_TRIGGER,
            SPELL_LIGHT_BULLET_TRIGGER_2,
            SPELL_LIGHT_BULLET_TIMER,
            SPELL_NUKE,
            SPELL_NUKE_GIGA,
            SPELL_CRUMBLING_EARTH,
            SPELL_FUNKY,
            SPELL_MANA_REDUCE,
            SPELL_CRITICAL_HIT,
            SPELL_BURST_2,
            SPELL_BURST_3,
            SPELL_BURST_4,
            SPELL_BURST_8,
            SPELL_BURST_X
    );

    // Wands
    public static final DeferredItem<WandItem> WAND_SMC_SC_NS = ITEMS.registerItem("wand_smc_sc_ns", properties -> new WandItem(
            properties.component(DataComponents.WAND_DATA.get(), new WandData(new WandStatistics(
                    false, 1, 2, 20, 2000, 20, 10, 0, new ArrayList<>(), 1
            )))));

    public static final DeferredItem<WandItem> WAND_LC_SC_S = ITEMS.registerItem("wand_lc_sc_s", properties -> new WandItem(
            properties.component(DataComponents.WAND_DATA.get(), new WandData(new WandStatistics(
                    true, 1, 2, 20, 2000, 20, 40, 0, new ArrayList<>(), 1
            )))));

    public static final DeferredItem<WandItem> WAND_MLC_3C_S = ITEMS.registerItem("wand_mlc_3c_s", properties -> new WandItem(
            properties.component(DataComponents.WAND_DATA.get(), new WandData(new WandStatistics(
                    true, 3, 2, 20, 2000, 20, 20, 10, new ArrayList<>(), 1
            )))));

    public static final DeferredItem<WandItem> WAND_OF_DESTRUCTION = ITEMS.registerItem("wand_of_destruction", properties -> new WandItem(
            properties.component(DataComponents.WAND_DATA.get(), new WandData(new WandStatistics(
                    false, 1, 7, -760, 1130, 40, 25, 0, List.of(
                    SPELL_NUKE
            ), 1
            )))));
}
