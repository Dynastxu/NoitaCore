package dynastxu.noitacore.datagen;

import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.item.CreativeTabs;
import dynastxu.noitacore.item.Items;
import net.minecraft.data.PackOutput;

import java.util.Arrays;

public final class ModLanguageProviderEnUS extends ModLanguageProvider {
    public ModLanguageProviderEnUS(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addCreativeTabs() {
        add(CreativeTabs.NOITA_SPELL_TAB, "Noita Spells");
        add(CreativeTabs.NOITA_WAND_TAB, "Noita Wands");
    }

    @Override
    protected void addItems() {
        // Projectile
        addSpell(Items.SPELL_RUBBER_BALL, "Bouncing Burst", "A very bouncy projectile");
        addSpell(Items.SPELL_LIGHT_BULLET, "Spark Bolt", "A weak but enchanting sparkling projectile");
        addSpell(Items.SPELL_LIGHT_BULLET_TRIGGER, "Spark Bolt With Trigger", "A spark bolt that casts another spell upon collision");
        addSpell(Items.SPELL_LIGHT_BULLET_TRIGGER_2, "Spark Bolt With Double Trigger", "A spark bolt that casts two new spells upon collision");
        addSpell(Items.SPELL_LIGHT_BULLET_TIMER, "Spark Bolt With Timer", "A spark bolt that casts another spell after a timer runs out");
        addSpell(Items.SPELL_NUKE, "Nuke", "Take cover!");
        addSpell(Items.SPELL_NUKE_GIGA, "Giga Nuke", "What do you expect?");
        addSpell(Items.SPELL_CRUMBLING_EARTH, "Earthquake", "Calls the anger of the earth");
        addSpell(Items.SPELL_FUNKY, "???", "???");
        addSpell(Items.SPELL_BLACK_HOLE, "Black Hole", "A slow orb of void that eats through all obstacles");
        addSpell(Items.SPELL_BLACK_HOLE_DEATH_TRIGGER, "Black Hole with Death Trigger", "A slow orb of void that eats through all obstacles and casts another spell as it expires");
        addSpell(Items.SPELL_BUCKSHOT, "Triplicate Bolt", "A formation of three small, fast bolts");
        addSpell(Items.SPELL_LASER_EMITTER, "Plasma Beam", "An instantaneous, dangerous beam of light");
        addSpell(Items.SPELL_TELEPORT_PROJECTILE, "Teleport Bolt", "A magical bolt that moves you wherever it ends up flying");
        addSpell(Items.SPELL_TELEPORT_PROJECTILE_SHORT, "Small Teleport Bolt", "A shortlived magical bolt that moves you wherever it ends up flying");

        // Modifier
        addSpell(Items.SPELL_MANA_REDUCE, "Add Mana", "Immediately adds 30 mana to the wand");
        addSpell(Items.SPELL_CRITICAL_HIT, "Critical Plus", "Gives a projectile +15% chance of a critical hit");
        addSpell(Items.SPELL_ORBIT_LASERS, "Plasma Beam Orbit", "Makes six plasma beams rotate around a projectile");
        addSpell(Items.SPELL_SPEED, "Speed Up", "Increases the speed at which a projectile flies through the air");
        addSpell(Items.SPELL_ACCELERATING_SHOT, "Accelerating Shot", "Causes a projectile to accelerate as it flies");
        addSpell(Items.SPELL_DECELERATING_SHOT, "Decelerating Shot", "Makes a projectile decelerate as it flies");

        // Multicast
        addSpell(Items.SPELL_BURST_2, "Double Spell", "Simultaneously casts 2 spells");
        addSpell(Items.SPELL_BURST_3, "Triple Spell", "Simultaneously casts 3 spells");
        addSpell(Items.SPELL_BURST_4, "Quadruple Spell", "Simultaneously casts 4 spells");
        addSpell(Items.SPELL_BURST_8, "Octuple Spell", "Simultaneously cast 8 spells");
        addSpell(Items.SPELL_BURST_X, "Myriad Spell", "Simultaneously casts as many spells as you have left uncast in your wand");

        // Wands
        add(Items.WAND_SMC_SC_NS.get(), "Wand - Small/Med Capacity - Single Cast - No-shuffle");
        add(Items.WAND_LC_SC_S.get(), "Wand - Large Capacity - Single Cast - Shuffle");
        add(Items.WAND_MLC_3C_S.get(), "Wand - Med/Large Capacity - 3 Cast - Shuffle");
        add(Items.WAND_OF_DESTRUCTION.get(), "WAND OF DESTRUCTION");
    }

    @Override
    protected void addDamageTypes() {
        addDamageTypeDefault("spell_projectile", "%1$s was killed by %2$s's spell projectiles", "%1$s was assassinated by spell projectile", "%1$s was killed by spell projectiles fired by %3$s from %2$s");
    }

    @Override
    protected void addToolTips() {
        addTooltip("spell_type", "Type");
        addTooltip("mana_drain", "Mana Drain");
        addTooltip("cast_delay", "Cast Delay");
        addTooltip("uses", "Uses");
        addTooltip("spread_modification", "Spread Modification");
        addTooltip("radius", "Radius");
        addTooltip("speed", "Speed");
        addTooltip("damage", "Damage");
        addTooltip("recharge_time", "Recharg. Time");
        addTooltip("crit", "Crit Chance Bonus");
        addTooltip("shuffle", "Shuffle");
        addTooltip("spells_per_cast", "Spells/Cast");
        addTooltip("mana_max", "Mana max");
        addTooltip("mana_charge_speed", "Mana Chg. Spd");
        addTooltip("capacity", "Capacity");
        addTooltip("spread", "Spread");
        addTooltip("explosion", "Explosion");
        addTooltip("always_casts", "Always Cast");
    }

    @Override
    protected void addEnums() {
        Arrays.stream(SpellType.values()).forEachOrdered(value -> {
            switch (value) {
                case Projectile -> add(value.getTranslationKey(), "Projectile");
                case Static -> add(value.getTranslationKey(), "Static");
                case Passive -> add(value.getTranslationKey(), "Passive");
                case Utility -> add(value.getTranslationKey(), "Utility");
                case Modifier -> add(value.getTranslationKey(), "Modifier");
                case Material -> add(value.getTranslationKey(), "Material");
                case Multicast -> add(value.getTranslationKey(), "Multicast");
                case Other -> add(value.getTranslationKey(), "Other");
            }
        });
    }
}
