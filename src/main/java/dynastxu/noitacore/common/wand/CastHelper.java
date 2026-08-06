package dynastxu.noitacore.common.wand;

import com.mojang.logging.LogUtils;
import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.common.spell.Spell;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.common.spell.UnitSpellChain;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.item.SpellItem;
import lombok.Getter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CastHelper {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final WandStatistics statistics;
    protected int mana;
    protected List<Spell> drawStack;
    protected List<Spell> preLoadStack;
    protected List<Spell> discardStack;
    protected int castDelayTick;
    protected int rechargeTick;
    private int preCastDelayTick;
    private int preRechargeTick;
    NonNullList<ItemStack> inventory;

    @Getter
    private WandData wandDataAfterCast;

    public CastHelper(@NonNull WandData wandData) {
        this.statistics = wandData.statistics();
        this.mana = statistics.manaMax();
        this.drawStack = wandData.drawStack();
        this.discardStack = wandData.discardStack();
        this.castDelayTick = statistics.castDelayTick();
        this.rechargeTick = statistics.rechargeTick();
        this.inventory = wandData.inventory();

        this.wandDataAfterCast = wandData;
        this.preLoadStack = new ArrayList<>();
        this.preCastDelayTick = 0;
        this.preRechargeTick = 0;
    }

    protected void genWandDataAfterCast() {
        wandDataAfterCast = new WandData(statistics, mana, inventory, drawStack, discardStack, castDelayTick, rechargeTick);
    }

    public boolean isCastDelaying() {
        return castDelayTick > 0;
    }

    public boolean isRecharging() {
        return rechargeTick > 0;
    }

    public boolean isCooling() {
        return isCastDelaying() || isRecharging();
    }

    public @NonNull List<Spell> getSpells() {
        List<Spell> result = new ArrayList<>();

        for (int i = 0; i < statistics.capacity(); i++) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof SpellItem) {
                SpellAttributes attributes = itemStack.getData(DataMaps.SPELL_ATTRIBUTES);
                if (attributes != null) {
                    result.add(new Spell(attributes, i));
                }
            }
        }

        return result;
    }

    public List<UnitSpellChain> getNextCast(Caster<?> caster) {
        if (isCooling()) {
            return null;
        }
        List<UnitSpellChain> result = new ArrayList<>();

        List<SpellAttributes> alwaysCasts = statistics.alwaysCasts();
        if (!alwaysCasts.isEmpty()) {
            drawStack.addAll(0, alwaysCasts.stream().map(Spell::new).toList());
            while (preLoadStack.size() < alwaysCasts.size()) {
                List<UnitSpellChain> nextChains = getNextSpellChains(caster, false);
                if (nextChains != null && !nextChains.isEmpty()) {
                    result.addAll(nextChains);
                } else {
                    onCastOver();
                    return result;
                }
            }
        }

        for (int i = 0; i < statistics.spellsPerCast(); i++) {
            List<UnitSpellChain> nextChains = getNextSpellChains(caster, false);
            if (nextChains != null && !nextChains.isEmpty()) {
                result.addAll(nextChains);
            } else {
                onCastOver();
                return result;
            }
        }

        onCastOver();
        return result;
    }

    protected void onCastOver() {
        discardStack.addAll(preLoadStack.stream().filter(spell -> !spell.isFromAlwaysCasts()).toList());
        preLoadStack = new ArrayList<>();

        if (drawStack.isEmpty()) {
            drawStack = new ArrayList<>(getSpells());
            discardStack = new ArrayList<>();
            if (statistics.shuffle()) {
                Collections.shuffle(drawStack);
            }
        }

        applyCastDelay();
        genWandDataAfterCast();
    }

    protected List<UnitSpellChain> getNextSpellChains(Caster<?> caster, boolean isSuffix) {
        if (isCooling()) {
            return new ArrayList<>();
        }
        if (drawStack.isEmpty()) {
            return new ArrayList<>();
        }
        List<UnitSpellChain> result = new ArrayList<>();

        List<SpellAttributes> modifiers = new ArrayList<>(); // 单独计算，不嵌套
        List<List<UnitSpellChain>> suffixes = new ArrayList<>();
        List<SpellAttributes> mainSpells = new ArrayList<>();
        int drawCount = 1;
        while (drawCount > 0) {
            // 回绕
            if (drawStack.isEmpty()) {
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
            if (mana < spell.attributes().base().manaDrain() && !isSuffix && !spell.isFromAlwaysCasts()) {
                discardStack.add(spell);
                continue;
            }
            if (spell.getDataFrom(inventory) != null) {
                final boolean mustConsume = spell.attributes().base().uses().mustConsume();
                if (spell.getDataFrom(inventory).remainingUses() <= 0) {
                    if (!caster.canSkipConsumeUses() || mustConsume) {
                        discardStack.add(spell);
                        continue;
                    }
                } else {
                    if (!caster.canSkipConsumeUses() || mustConsume) {
                        spell.consume(inventory);
                    }
                }
            }
            preLoadStack.add(spell);
            if (!isSuffix) {
                addRecharge(spell.attributes().base().rechargeTick());
                if (spell.attributes().base().type() == SpellType.Projectile) {
                    addCastDelay(spell.attributes().base().castDelayTick());
                }
                if (!spell.isFromAlwaysCasts() || spell.attributes().base().manaDrain() < 0) {
                    mana -= spell.attributes().base().manaDrain();
                }
            }
            drawCount--;

            if (spell.attributes().isModifier()) {
                modifiers.add(spell.attributes());
            } else if (spell.attributes().isMulticast()) {
                drawCount += spell.attributes().base().draws() - (spell.isFromAlwaysCasts() ? 1 : 0);
            } else {
                SpellAttributes mainSpell = spell.attributes();
                mainSpells.add(mainSpell);
                int suffixNum = mainSpell.suffix().num();
                if (suffixNum > 0) {
                    List<UnitSpellChain> suf = getSuffix(caster, suffixNum);
                    if (!suf.isEmpty()) {
                        suffixes.add(suf);
                    } else {
                        suffixes.add(null);
                    }
                }
            }
        }

        AtomicInteger i = new AtomicInteger();
        mainSpells.forEach(spell -> {
            result.add(new UnitSpellChain(spell, suffixes.get(i.get()), modifiers));
            i.getAndIncrement();
        });

        return result;
    }

    private @NonNull List<UnitSpellChain> getSuffix(Caster<?> caster, int num) {
        List<UnitSpellChain> result = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            List<UnitSpellChain> chains = getNextSpellChains(caster, true);
            if (chains != null && !chains.isEmpty()) {
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

    public record Caster<T>(
            T caster
    ) {
        boolean canSkipConsumeUses() {
            return false;
        }
    }
}
