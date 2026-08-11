package dynastxu.noitacore.datagen;

import dynastxu.noitacore.CreativeTabs;
import dynastxu.noitacore.item.Items;
import net.minecraft.data.PackOutput;

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
        add(Items.SPELL_RUBBER_BALL.get(), "Bouncing Burst");
        add(Items.SPELL_LIGHT_BULLET.get(), "Spark Bolt");
        add(Items.WAND_SMC_SC_NS.get(), "Wand - Small/Med Capacity - Single Cast - No-shuffle");
        add(Items.WAND_LC_SC_S.get(), "Wand - Large Capacity - Single Cast - Shuffle");
    }

    @Override
    protected void addDamageTypes() {
        addDamageTypeDefault("spell_projectile", "%1$s was killed by %2$s's spell projectiles", "%1$s was assassinated by spell projectile", "%1$s was killed by spell projectiles fired by %3$s from %2$s");
    }
}
