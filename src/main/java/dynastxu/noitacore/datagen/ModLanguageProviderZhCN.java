package dynastxu.noitacore.datagen;

import dynastxu.noitacore.block.Blocks;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.item.Items;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;

public final class ModLanguageProviderZhCN extends ModLanguageProvider {
    public ModLanguageProviderZhCN(PackOutput output) {
        super(output, "zh_cn");
    }

    @Override
    protected void addCreativeTabs() {
        addCreativeModeTab("noita_spells", "女巫：法术");
        addCreativeModeTab("noita_blocks", "女巫：方块");
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
        addSpell(Items.SPELL_FUNKY, "???", "???");
        addSpell(Items.SPELL_BLACK_HOLE, "黑洞", "缓慢的虚无球体，吞噬沿路的一切材料");
        addSpell(Items.SPELL_BLACK_HOLE_DEATH_TRIGGER, "带有死亡触发的黑洞", "在失效时释放另一法术的黑洞");
        addSpell(Items.SPELL_BUCKSHOT, "三联魔弹", "三颗高速且带有弹性的小子弹");
        addSpell(Items.SPELL_LASER_EMITTER, "电浆束", "一束持续短时间的致命光线");
        addSpell(Items.SPELL_TELEPORT_PROJECTILE, "传送魔弹", "将你传送到其失效时的位置");
        addSpell(Items.SPELL_TELEPORT_PROJECTILE_SHORT, "小传送魔弹", "存在时间更短、速度更快的传送魔弹");
        addSpell(Items.SPELL_DISC_BULLET, "碟状投射物", "锐利且富有弹性的的锯片");
        addSpell(Items.SPELL_DISC_BULLET_BIG, "巨型碟状投射物", "更大号的锯片！而且它好像关于如何飞行有它自己的想法...");

        // Modifier
        addSpell(Items.SPELL_MANA_REDUCE.get(), "额外法力", "立即向魔杖注入 30 法力");
        addSpell(Items.SPELL_CRITICAL_HIT.get(), "暴击率+", "提高投射物的暴击率");
        addSpell(Items.SPELL_ORBIT_LASERS, "电浆束环绕", "令六道电浆束绕一个投射物旋转");
        addSpell(Items.SPELL_SPEED, "加速", "增加投射物的初始速度");
        addSpell(Items.SPELL_ACCELERATING_SHOT, "加速魔弹", "降低投射物初速度，但提高投射物的加速度");
        addSpell(Items.SPELL_DECELERATING_SHOT, "减速魔弹", "增加投射物的初速度，但也增加飞行时受到的阻力");

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
        add(Items.WAND_OF_DESTRUCTION.get(), "毁灭之杖");

        add(Items.NOITA_BOOK.get(), "女巫之书");
    }

    @Override
    protected void addBlocks() {
        addWithSlabAndStair(Blocks.BRICKWORK, Blocks.BRICKWORK_SLAB, Blocks.BRICKWORK_STAIR, "砖块");
    }

    @Override
    protected void addDamageTypes() {
        addDamageTypeDefault("spell_projectile", "%1$s死于%2$s的弹射物", "%1$s被弹射物暗杀", "%1$s死于%2$s使用%3$s发射的弹射物");
        addDamageTypeDefault("spell_slice", "%1$s被%2$s切成了碎片", "%1$s的细胞们有了自己的想法", "%1$s被%2$s使用%3$s切成了碎片");
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
        addTooltip("always_casts", "始终施放");
        addTooltip("durability", "破坏等级");
        addTooltip("density", "密度");
        addTooltip("hardness", "材料血量");
        addTooltip("conductive", "导电性");
        addTooltip("unknown", "未知");
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

    @Override
    protected void addBookContents() {
        addBookContent("overview", "概览");
        addBookContent("overview.spells_unlocked", "法术解锁进度");
        addBookContent("contents_table", "目录");
        addBookContent("spells", "法术");
    }

    @Override
    protected void addCommands() {
        addCommand("argument_invalid", "参数无效");
        addCommand("spell_unlocked", "已解锁法术");
        addCommand("spell_unlock_all", "已解锁所有法术");
        addCommand("spell_locked", "已锁定法术");
        addCommand("spell_lock_all", "已锁定所有法术");
    }

    private void addWithSlabAndStair(@NonNull DeferredBlock<?> baseBlock, DeferredBlock<?> slabBlock, DeferredBlock<?> stairBlock, String name) {
        addWithSlabAndStair(baseBlock, slabBlock, stairBlock, name, "台阶", "楼梯");
    }
}
