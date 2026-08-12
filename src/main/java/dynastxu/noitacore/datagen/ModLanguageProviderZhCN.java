package dynastxu.noitacore.datagen;

import dynastxu.noitacore.item.CreativeTabs;
import dynastxu.noitacore.item.Items;
import net.minecraft.data.PackOutput;

public final class ModLanguageProviderZhCN extends ModLanguageProvider {
    public ModLanguageProviderZhCN(PackOutput output) {
        super(output, "zh_cn");
    }

    @Override
    protected void addCreativeTabs() {
        add(CreativeTabs.NOITA_SPELL_TAB, "女巫：法术");
        add(CreativeTabs.NOITA_WAND_TAB, "女巫：法杖");
    }

    @Override
    protected void addItems() {
        // Spells
        // Projectile
        add(Items.SPELL_RUBBER_BALL.get(), "弹跳绿豆");
        add(Items.SPELL_LIGHT_BULLET.get(), "火花弹");
        // Multicast
        add(Items.SPELL_BURST_2.get(), "二重施法");
        add(Items.SPELL_BURST_3.get(), "三重施法");
        add(Items.SPELL_BURST_4.get(), "四重施法");
        add(Items.SPELL_BURST_8.get(), "八重施法");
        add(Items.SPELL_BURST_X.get(), "穷尽施法");
        // Wands
        add(Items.WAND_SMC_SC_NS.get(), "法杖 - 小/中容量 - 单施法 - 有序");
        add(Items.WAND_LC_SC_S.get(), "法杖 - 大容量 - 单施法 - 无序");
    }

    @Override
    protected void addDamageTypes() {
        addDamageTypeDefault("spell_projectile", "%1$s死于%2$s的弹射物", "%1$s被弹射物暗杀", "%1$s死于%2$s使用%3$s发射的弹射物");
    }
}
