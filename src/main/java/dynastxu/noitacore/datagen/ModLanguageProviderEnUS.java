package dynastxu.noitacore.datagen;

import dynastxu.noitacore.CreativeTabs;
import dynastxu.noitacore.item.Items;
import net.minecraft.data.PackOutput;

public final class ModLanguageProviderEnUS extends ModLanguageProvider {
    public ModLanguageProviderEnUS(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addTranslations() {
        addCreativeTabs();
        addItems();
    }

    @Override
    protected void addCreativeTabs() {
        add(CreativeTabs.NOITA_SPELL_TAB, "Noita Spells");
        add(CreativeTabs.NOITA_WAND_TAB, "Noita Wands");
    }

    @Override
    protected void addItems() {
        add(Items.SPELL_RUBBER_BALL.get(), "Bouncing Burst Spell");
        add(Items.WAND_SMC_SC_NS.get(), "Wand - Small/Med Capacity - Single Cast - No-shuffle");
        add(Items.WAND_LC_SC_S.get(), "Wand - Large Capacity - Single Cast - Shuffle");
    }
}
