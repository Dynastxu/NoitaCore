package dynastxu.noitacore.datagen;

import dynastxu.noitacore.CreativeTabs;
import dynastxu.noitacore.item.Items;
import net.minecraft.data.PackOutput;

public final class ModLanguageProviderZhCN extends ModLanguageProvider {
    public ModLanguageProviderZhCN(PackOutput output) {
        super(output, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        addCreativeTabs();
        addItems();
    }

    @Override
    protected void addCreativeTabs() {
        add(CreativeTabs.NOITA_SPELL_TAB, "女巫：法术");
        add(CreativeTabs.NOITA_WAND_TAB, "女巫：法杖");
    }

    @Override
    protected void addItems() {
        add(Items.BOUNCING_BURST_SPELL.get(), "弹跳爆发");
        add(Items.WAND_SMC_SC_NS.get(), "法杖 - 小/中容量 - 单施法 - 有序");
        add(Items.WAND_LC_SC_S.get(), "法杖 - 大容量 - 单施法 - 无序");
    }
}
