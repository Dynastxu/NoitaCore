package dynastxu.noitacore.datagen;

import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.item.CreativeTabs;
import dynastxu.noitacore.item.Items;
import net.minecraft.data.PackOutput;

import java.util.Arrays;

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
        // Projectile
        addSpell(Items.SPELL_RUBBER_BALL.get(), "弹跳绿豆", "极具弹跳力的投射物");
        addSpell(Items.SPELL_LIGHT_BULLET.get(), "火花弹", "弱小但带有迷人闪烁的投射物");
        addSpell(Items.SPELL_LIGHT_BULLET_TRIGGER.get(), "带有触发的火花弹", "在碰撞时施放另一法术的火花弹");
        addSpell(Items.SPELL_LIGHT_BULLET_TRIGGER_2.get(), "带有双重触发的火花弹", "在碰撞时施放另外两个法术的火花弹");
        addSpell(Items.SPELL_LIGHT_BULLET_TIMER.get(), "带有定时的火花弹", "在定时结束后释放另一法术的火花弹");
        addSpell(Items.SPELL_NUKE.get(), "核弹", "快找掩体！");
        addSpell(Items.SPELL_NUKE_GIGA.get(), "巨型核弹", "你在期待什么呢？");
        addSpell(Items.SPELL_CRUMBLING_EARTH.get(), "地震", "呼唤大地的愤怒");

        // Modifier
        addSpell(Items.SPELL_MANA_REDUCE.get(), "额外法力", "立即向魔杖注入 30 法力");
        addSpell(Items.SPELL_CRITICAL_HIT.get(), "暴击率+", "提高投射物的暴击率");
        // Multicast
        addSpell(Items.SPELL_BURST_2.get(), "二重施法", "同时施放2个法术");
        addSpell(Items.SPELL_BURST_3.get(), "三重施法", "同时施放3个法术");
        addSpell(Items.SPELL_BURST_4.get(), "四重施法", "同时施放4个法术");
        addSpell(Items.SPELL_BURST_8.get(), "八重施法", "同时施放8个法术");
        addSpell(Items.SPELL_BURST_X.get(), "穷尽施法", "同时施放魔杖中剩余的所有法术");

        // Wands
        add(Items.WAND_SMC_SC_NS.get(), "法杖 - 小/中容量 - 单施法 - 有序");
        add(Items.WAND_LC_SC_S.get(), "法杖 - 大容量 - 单施法 - 无序");
        add(Items.WAND_MLC_3C_S.get(), "法杖 - 中/大容量 - 3 施法 - 无序");
    }

    @Override
    protected void addDamageTypes() {
        addDamageTypeDefault("spell_projectile", "%1$s死于%2$s的弹射物", "%1$s被弹射物暗杀", "%1$s死于%2$s使用%3$s发射的弹射物");
    }

    @Override
    protected void addToolTips() {
        addTooltip("spell_type", "类型");
        addTooltip("mana_drain", "法力消耗");
        addTooltip("cast_delay", "施放延迟");
        addTooltip("uses", "使用次数");
        addTooltip("spread_modification", "散射角度");
        addTooltip("radius", "半径");
        addTooltip("speed", "速度");
        addTooltip("damage", "伤害");
        addTooltip("recharge_time", "充能时间");
        addTooltip("crit", "暴击率");
        addTooltip("shuffle", "乱序");
        addTooltip("spells_per_cast", "法术数/施放");
        addTooltip("mana_max", "法力最大值");
        addTooltip("mana_charge_speed", "法力充能速度");
        addTooltip("capacity", "容量");
        addTooltip("spread", "散射");
        addTooltip("explosion", "爆炸伤害");
    }

    @Override
    protected void addEnums() {
        Arrays.stream(SpellType.values()).forEachOrdered(type -> {
            switch (type) {
                case Projectile -> add(type.getTranslationKey(), "投射物");
                case Static -> add(type.getTranslationKey(), "静态投射物");
                case Passive -> add(type.getTranslationKey(), "被动");
                case Utility -> add(type.getTranslationKey(), "实用");
                case Modifier -> add(type.getTranslationKey(), "投射修正");
                case Material -> add(type.getTranslationKey(), "材料");
                case Multicast -> add(type.getTranslationKey(), "多重释放");
                case Other -> add(type.getTranslationKey(), "其他");
            }
        });
    }
}
