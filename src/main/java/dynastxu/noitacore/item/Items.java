package dynastxu.noitacore.item;

import dynastxu.noitacore.block.Blocks;
import dynastxu.noitacore.common.wand.WandStatistics;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.entity.EntityTypes;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Spells
    // Projectile
    public static final DeferredItem<SpellItem.Projectile> SPELL_RUBBER_BALL = ITEMS.registerItem("spell_rubber_ball", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.RUBBER_BALL.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LIGHT_BULLET = ITEMS.registerItem("spell_light_bullet", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LIGHT_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LIGHT_BULLET_TRIGGER = ITEMS.registerItem("spell_light_bullet_trigger", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LIGHT_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LIGHT_BULLET_TRIGGER_2 = ITEMS.registerItem("spell_light_bullet_trigger_2", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LIGHT_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LIGHT_BULLET_TIMER = ITEMS.registerItem("spell_light_bullet_timer", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LIGHT_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_NUKE = ITEMS.registerItem("spell_nuke", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(1).build()),
            EntityTypes.NUKE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_NUKE_GIGA = ITEMS.registerItem("spell_nuke_giga", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(1).build()),
            EntityTypes.NUKE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_CRUMBLING_EARTH = ITEMS.registerItem("spell_crumbling_earth", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(3).build()),
            EntityTypes.CRUMBLING_EARTH.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_FUNKY = ITEMS.registerItem("spell_funky", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.FUNKY.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_BLACK_HOLE = ITEMS.registerItem("spell_black_hole", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(3).build()),
            EntityTypes.BLACK_HOLE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_BLACK_HOLE_DEATH_TRIGGER = ITEMS.registerItem("spell_black_hole_death_trigger", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(3).build()),
            EntityTypes.BLACK_HOLE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_BUCKSHOT = ITEMS.registerItem("spell_buckshot", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.BUCKSHOT.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LASER_EMITTER = ITEMS.registerItem("spell_laser_emitter", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LASER_EMITTER.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_TELEPORT_PROJECTILE = ITEMS.registerItem("spell_teleport_projectile", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.TELEPORT_PROJECTILE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_TELEPORT_PROJECTILE_SHORT = ITEMS.registerItem("spell_teleport_projectile_short", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.TELEPORT_PROJECTILE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_DISC_BULLET = ITEMS.registerItem("spell_disc_bullet", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.DISC_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_DISC_BULLET_BIG = ITEMS.registerItem("spell_disc_bullet_big", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.DISC_BULLET_BIG.get()));

    // Modifier
    public static final DeferredItem<SpellItem.Modifier> SPELL_MANA_REDUCE = ITEMS.registerItem("spell_mana_reduce", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Modifier> SPELL_CRITICAL_HIT = ITEMS.registerItem("spell_critical_hit", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<OrbitLasersSpellItem> SPELL_ORBIT_LASERS = ITEMS.registerItem("spell_orbit_lasers", properties -> new OrbitLasersSpellItem(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Modifier> SPELL_SPEED = ITEMS.registerItem("spell_speed", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Modifier> SPELL_ACCELERATING_SHOT = ITEMS.registerItem("spell_accelerating_shot", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Modifier> SPELL_DECELERATING_SHOT = ITEMS.registerItem("spell_decelerating_shot", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    // Multicast
    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_2 = ITEMS.registerItem("spell_burst_2", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_3 = ITEMS.registerItem("spell_burst_3", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_4 = ITEMS.registerItem("spell_burst_4", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_8 = ITEMS.registerItem("spell_burst_8", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_X = ITEMS.registerItem("spell_burst_x", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(30).build())));

    public static final List<DeferredItem<? extends SpellItem>> SPELL_ITEMS = List.of(
            // Projectile
            SPELL_RUBBER_BALL,
            SPELL_LIGHT_BULLET,
            SPELL_LIGHT_BULLET_TRIGGER,
            SPELL_LIGHT_BULLET_TRIGGER_2,
            SPELL_LIGHT_BULLET_TIMER,
            SPELL_NUKE,
            SPELL_NUKE_GIGA,
            SPELL_CRUMBLING_EARTH,
            SPELL_FUNKY,
            SPELL_BLACK_HOLE,
            SPELL_BLACK_HOLE_DEATH_TRIGGER,
            SPELL_BUCKSHOT,
            SPELL_LASER_EMITTER,
            SPELL_TELEPORT_PROJECTILE,
            SPELL_TELEPORT_PROJECTILE_SHORT,
            SPELL_DISC_BULLET,
            SPELL_DISC_BULLET_BIG,

            // Modifier
            SPELL_MANA_REDUCE,
            SPELL_CRITICAL_HIT,
            SPELL_ORBIT_LASERS,
            SPELL_SPEED,
            SPELL_ACCELERATING_SHOT,
            SPELL_DECELERATING_SHOT,

            // Multicast
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

    public static final DeferredItem<BlockItem> BRICKWORK = ITEMS.registerItem("brickwork", properties -> new BlockItem(Blocks.BRICKWORK.get(), properties));
}
