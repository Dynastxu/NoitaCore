package dynastxu.noitacore.item;

import dynastxu.noitacore.block.Blocks;
import dynastxu.noitacore.common.wand.WandStatistics;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.entity.EntityTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final List<DeferredItem<? extends SpellItem>> SPELL_ITEMS = new ArrayList<>();

    // Spells
    // Projectile
    public static final DeferredItem<SpellItem.Projectile> SPELL_RUBBER_BALL = registerSpell("spell_rubber_ball", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.RUBBER_BALL.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LIGHT_BULLET = registerSpell("spell_light_bullet", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LIGHT_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LIGHT_BULLET_TRIGGER = registerSpell("spell_light_bullet_trigger", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LIGHT_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LIGHT_BULLET_TRIGGER_2 = registerSpell("spell_light_bullet_trigger_2", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LIGHT_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LIGHT_BULLET_TIMER = registerSpell("spell_light_bullet_timer", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LIGHT_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_NUKE = registerSpell("spell_nuke", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(1).build()),
            EntityTypes.NUKE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_NUKE_GIGA = registerSpell("spell_nuke_giga", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(1).build()),
            EntityTypes.NUKE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_CRUMBLING_EARTH = registerSpell("spell_crumbling_earth", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(3).build()),
            EntityTypes.CRUMBLING_EARTH.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_FUNKY = registerSpell("spell_funky", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.FUNKY.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_BLACK_HOLE = registerSpell("spell_black_hole", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(3).build()),
            EntityTypes.BLACK_HOLE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_BLACK_HOLE_DEATH_TRIGGER = registerSpell("spell_black_hole_death_trigger", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(3).build()),
            EntityTypes.BLACK_HOLE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_BUCKSHOT = registerSpell("spell_buckshot", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.BUCKSHOT.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_LASER_EMITTER = registerSpell("spell_laser_emitter", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.LASER_EMITTER.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_TELEPORT_PROJECTILE = registerSpell("spell_teleport_projectile", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.TELEPORT_PROJECTILE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_TELEPORT_PROJECTILE_SHORT = registerSpell("spell_teleport_projectile_short", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.TELEPORT_PROJECTILE.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_DISC_BULLET = registerSpell("spell_disc_bullet", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.DISC_BULLET.get()));

    public static final DeferredItem<SpellItem.Projectile> SPELL_DISC_BULLET_BIG = registerSpell("spell_disc_bullet_big", properties -> new SpellItem.Projectile(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build()),
            EntityTypes.DISC_BULLET_BIG.get()));

    // Modifier
    public static final DeferredItem<SpellItem.Modifier> SPELL_MANA_REDUCE = registerSpell("spell_mana_reduce", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Modifier> SPELL_CRITICAL_HIT = registerSpell("spell_critical_hit", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<OrbitLasersSpellItem> SPELL_ORBIT_LASERS = registerSpell("spell_orbit_lasers", properties -> new OrbitLasersSpellItem(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Modifier> SPELL_SPEED = registerSpell("spell_speed", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Modifier> SPELL_ACCELERATING_SHOT = registerSpell("spell_accelerating_shot", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Modifier> SPELL_DECELERATING_SHOT = registerSpell("spell_decelerating_shot", properties -> new SpellItem.Modifier(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    // Multicast
    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_2 = registerSpell("spell_burst_2", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_3 = registerSpell("spell_burst_3", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_4 = registerSpell("spell_burst_4", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_8 = registerSpell("spell_burst_8", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().build())));

    public static final DeferredItem<SpellItem.Multicast> SPELL_BURST_X = registerSpell("spell_burst_x", properties -> new SpellItem.Multicast(
            properties.component(DataComponents.SPELL_DATA.get(), SpellData.builder().remainingUses(30).build())));

    private static <I extends SpellItem> @NonNull DeferredItem<I> registerSpell(String name, Function<Item.Properties, I> fun) {
        DeferredItem<I> value = ITEMS.registerItem(name, fun);
        SPELL_ITEMS.add(value);
        return value;
    }

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
