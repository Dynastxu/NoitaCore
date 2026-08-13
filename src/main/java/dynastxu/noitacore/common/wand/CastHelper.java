package dynastxu.noitacore.common.wand;

import com.mojang.logging.LogUtils;
import dynastxu.noitacore.common.spell.Spell;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.common.spell.UnitSpellChain;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.utils.Utils;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CastHelper {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final WandStatistics statistics;
    private final NonNullList<ItemStack> inventory;
    protected int mana;
    protected List<Spell> drawStack;
    protected List<Spell> preLoadStack;
    protected List<Spell> discardStack;
    protected int castDelayTick;
    protected int rechargeTick;
    private int preCastDelayTick;
    private int preRechargeTick;
    @Getter
    protected float spread;
    @Getter
    protected float critChance;
    @Getter
    protected WandData wandDataAfterCast;
    private boolean isUsed;

    public CastHelper(@NonNull WandData wandData) {
        this.statistics = wandData.statistics();
        this.mana = wandData.mana();
        this.drawStack = new ArrayList<>(wandData.drawStack());
        this.discardStack = new ArrayList<>(wandData.discardStack());
        this.castDelayTick = wandData.castDelayTick();
        this.rechargeTick = wandData.rechargeTick();
        this.inventory = Utils.clone(wandData.inventory());

        this.wandDataAfterCast = wandData;
        this.preLoadStack = new ArrayList<>();
        this.preCastDelayTick = 0;
        this.preRechargeTick = 0;
        this.spread = statistics.spread();
        this.isUsed = false;

        if (this.drawStack.isEmpty()) {
            this.drawStack = getSpells();
        }
    }

    protected void genWandDataAfterCast() {
        wandDataAfterCast = new WandData(statistics, mana, inventory, drawStack, discardStack, castDelayTick, rechargeTick, castDelayTick, rechargeTick);
    }

    protected boolean isCastDelaying() {
        return castDelayTick > 0;
    }

    protected boolean isRecharging() {
        return rechargeTick > 0;
    }

    protected boolean isCooling() {
        return isCastDelaying() || isRecharging();
    }

    protected @NonNull List<Spell> getSpells() {
        return WandData.getSpells(statistics.capacity(), inventory);
    }

    public @NonNull List<UnitSpellChain> getNextCast(Entity caster) {
        return getNextCast(new Caster<>(caster));
    }

    public @NonNull List<UnitSpellChain> getNextCast(Caster<?> caster) {
        if (isUsed) {
            throw new IllegalStateException("CastHelper is already used, please new one");
        } else {
            isUsed = true;
        }
        LOGGER.debug("开始获取施法内容");
        if (isCooling()) {
            LOGGER.debug("当前正在冷却中，无法施法");
            LOGGER.debug("当前剩余延迟刻：{}", castDelayTick);
            LOGGER.debug("当前剩余充能刻：{}", rechargeTick);
            return new ArrayList<>();
        }
        List<UnitSpellChain> result = new ArrayList<>();

        List<Holder<Item>> alwaysCasts = statistics.alwaysCasts();

        LOGGER.debug("抽牌堆：{}", drawStack.stream().map(Spell::getName).collect(Collectors.joining(", ")));

        if (!alwaysCasts.isEmpty()) {
            LOGGER.debug("处理始终施放");
            for (int i = 0; i < alwaysCasts.size(); i++) {
                Spell spell = new Spell(alwaysCasts.get(i), i, true);
                drawStack.add(i, spell);
            }
            List<UnitSpellChain> nextChains = getNextSpellChains(caster, false);
            if (!nextChains.isEmpty()) {
                result.addAll(nextChains);
            } else {
                onCastOver();
                return result;
            }
        }

        LOGGER.debug("处理普通施放");
        List<UnitSpellChain> nextChains = getNextSpellChains(caster, false);
        if (!nextChains.isEmpty()) {
            result.addAll(nextChains);
        } else {
            onCastOver();
            return result;
        }

        onCastOver();
        return result;
    }

    protected void onCastOver() {
        LOGGER.debug("获取结束");
        discardStack.addAll(preLoadStack.stream().filter(spell -> !spell.isAlwaysCast()).toList());
        preLoadStack = new ArrayList<>();

        if (drawStack.isEmpty()) {
            drawStack = new ArrayList<>(getSpells());
            discardStack = new ArrayList<>();
            if (statistics.shuffle()) {
                Collections.shuffle(drawStack);
            }
        }

        mana = Math.min(mana, statistics.manaMax());

        applyCastDelay();
        genWandDataAfterCast();
    }

    protected @NonNull List<UnitSpellChain> getNextSpellChains(Caster<?> caster, boolean isSuffix) {
        LOGGER.debug("尝试获取法术链{}", isSuffix ? "（后缀）" : "");
        List<UnitSpellChain> result = new ArrayList<>();

        List<Holder<Item>> modifiers = new ArrayList<>(); // 单独计算，不嵌套
        List<List<UnitSpellChain>> suffixes = new ArrayList<>();
        List<Holder<Item>> mainSpells = new ArrayList<>();
        int drawCount = Math.min(statistics.spellsPerCast(), drawStack.size());
        boolean isWraped = false;
        while (drawCount > 0) {
            // 回绕
            if (drawStack.isEmpty()) {
                if (isWraped) {
                    drawStack = getSpells();
                    discardStack = new ArrayList<>();
                    break;
                }
                LOGGER.debug("抽牌堆已无法术，尝试回绕");
                isWraped = true;
                if (discardStack.isEmpty()) {
                    applyCooldown();
                    break;
                }
                drawStack = discardStack;
                discardStack = new ArrayList<>();
                if (statistics.shuffle()) {
                    Collections.shuffle(drawStack);
                }
            }

            Spell spell = drawStack.removeFirst();
            SpellAttributes spellAttributes = spell.getAttributes();
            if (spellAttributes == null) {
                LOGGER.error("出现无属性的法术，已跳过");
                discardStack.add(spell);
                continue;
            }
            if (mana < spellAttributes.base().manaDrain() && !isSuffix && !spell.isAlwaysCast()) {
                LOGGER.debug("法力值不足（当前法术：{}，消耗：{}，当前：{}）", spell.getName(), spellAttributes.base().manaDrain(), mana);
                discardStack.add(spell);
                continue;
            }
            if (!spell.isAlwaysCast() && spellAttributes.base().uses().uses() > 0) {
                SpellData spellData = spell.getDataFrom(inventory);
                if (spellData == null) {
                    LOGGER.error("出现无数据的法术，已跳过");
                    discardStack.add(spell);
                    continue;
                }
                final boolean mustConsume = spellAttributes.base().uses().mustConsume();
                if (spellData.remainingUses() <= 0) {
                    if (!caster.canSkipConsumeUses() || mustConsume) {
                        LOGGER.debug("施放次数已耗尽（当前法术：{}）", spell.getName());
                        discardStack.add(spell);
                        continue;
                    }
                } else {
                    if (!caster.canSkipConsumeUses() || mustConsume) {
                        spell.consume(inventory);
                    }
                }
            }

            LOGGER.debug("抽取{}：{}", spell.isAlwaysCast() ? "（始终施放）" : "", spell.getName());
            preLoadStack.add(spell);

            if (spellAttributes.modifications() != null) {
                spread += spellAttributes.modifications().spread();
                critChance += spellAttributes.modifications().criticalChance();
            }

            if (!isSuffix) {
                addRecharge(spell.getAttributes().base().rechargeTick());
                if (spell.getAttributes().base().type() == SpellType.Projectile) {
                    addCastDelay(spell.getAttributes().base().castDelayTick());
                }
                if (!spell.isAlwaysCast() || spell.getAttributes().base().manaDrain() < 0) {
                    mana -= spell.getAttributes().base().manaDrain();
                }
            }
            drawCount--;

            if (spell.getAttributes().isModifier()) {
                drawCount += spell.getAttributes().base().draws(); // 通常是 1
                modifiers.add(spell.itemHolder());
            } else if (spell.getAttributes().isMulticast()) {
                drawCount += spell.getAttributes().base().draws() - (spell.isAlwaysCast() ? 1 : 0); // 始终施放需要 -1
            } else {
                drawCount += spell.getAttributes().base().draws(); // 通常是 0
                mainSpells.add(spell.itemHolder());
                List<UnitSpellChain> suffix = new ArrayList<>();
                if (spell.getAttributes().suffix() != null) {
                    int suffixNum = spell.getAttributes().suffix().num();
                    if (suffixNum > 0) {
                        List<UnitSpellChain> suf = getSuffix(caster, suffixNum);
                        if (!suf.isEmpty()) {
                            suffix = suf;
                        }
                    }
                }
                suffixes.add(suffix);
            }
        }

        for (int i = 0; i < mainSpells.size(); i++) {
            result.add(new UnitSpellChain(mainSpells.get(i), suffixes.get(i), modifiers));
        }

        return result;
    }

    protected @NonNull List<UnitSpellChain> getSuffix(Caster<?> caster, int num) {
        LOGGER.debug("尝试获取后缀，数量：{}", num);
        List<UnitSpellChain> result = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            LOGGER.debug("尝试获取后缀，第 {} 个", i);
            List<UnitSpellChain> chains = getNextSpellChains(caster, true);
            if (!chains.isEmpty()) {
                result.addAll(chains);
            } else {
                break;
            }
        }

        return result;
    }

    private void addRecharge(int modify) {
        preRechargeTick += modify;
    }

    private void addCastDelay(int modify) {
        preCastDelayTick += statistics.castDelayTick() + modify;
    }

    private void applyRecharge() {
        rechargeTick = preRechargeTick + statistics.rechargeTick();
        preRechargeTick = 0;
        if (rechargeTick < 0) {
            rechargeTick = 0;
        }
    }

    private void applyCastDelay() {
        castDelayTick = preCastDelayTick;
        preCastDelayTick = 0;
        if (castDelayTick < 0) {
            castDelayTick = 0;
        }
    }

    private void applyCooldown() {
        if (isCooling()) return;
        applyCastDelay();
        applyRecharge();
    }
}
