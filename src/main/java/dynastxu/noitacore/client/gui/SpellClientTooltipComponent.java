package dynastxu.noitacore.client.gui;

import dynastxu.noitacore.client.font.Font;
import dynastxu.noitacore.client.language.EnglishTranslationCache;
import dynastxu.noitacore.common.spell.DamageType;
import dynastxu.noitacore.common.wand.Caster;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.datamap.SpellAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static dynastxu.noitacore.NoitaCore.ALT_FONT;
import static dynastxu.noitacore.NoitaCore.MODID;

public class SpellClientTooltipComponent extends AbstractStatClientTooltipComponent {

    public SpellClientTooltipComponent(@NonNull SpellTooltipComponent component) {
        super(buildStatLines(component.attributes(), component.spellData(), component.player(), component.unlocked()));
    }

    private static @NonNull List<StatLine> buildStatLines(@NonNull SpellAttributes attrs, SpellData spellData, Player player, boolean unlocked) {
        List<StatLine> lines = new ArrayList<>();

        if (unlocked) {
            lines.add(new StatLine(
                    Component.literal(Font.TYPE + " ")
                            .append(Component.translatable("tooltip.noitacore.spell_type")),
                    attrs.base().type().getTranslatedName()));

            if (attrs.base().uses().uses() > 0 && !Caster.canSkipConsumeUses(player, attrs)) {
                Component value;
                if (spellData != null) {
                    value = Component.literal(String.valueOf(spellData.remainingUses()));
                    if (spellData.remainingUses() == 0) {
                        value = value.copy().withStyle(ChatFormatting.RED);
                    } else if (spellData.remainingUses() < attrs.base().uses().uses()) {
                        value = value.copy().withStyle(ChatFormatting.YELLOW);
                    } else {
                        value = value.copy().withStyle(ChatFormatting.GREEN);
                    }
                } else {
                    value = Component.literal(String.valueOf(attrs.base().uses().uses()));
                }
                lines.add(new StatLine(
                        Component.literal(Font.USES + " ")
                                .append(Component.translatable("tooltip.noitacore.uses")),
                        value));
            }

            lines.add(new StatLine(
                    Component.literal(Font.MANA_DRAIN + " ")
                            .append(Component.translatable("tooltip.noitacore.mana_drain")),
                    Component.literal(String.valueOf(attrs.base().manaDrain()))));

            lines.add(new StatLine(
                    Component.literal(Font.CAST_DELAY + " ")
                            .append(Component.translatable("tooltip.noitacore.cast_delay")),
                    Component.literal(String.format("%.2fs", (float) attrs.base().castDelayTick() / 20))));

            lines.add(new StatLine(
                    Component.literal(Font.RECHARGE + " ")
                            .append(Component.translatable("tooltip.noitacore.recharge_time")),
                    Component.literal(String.format("%.2fs", (float) attrs.base().rechargeTick() / 20))));

            if (attrs.modifications() != null) {
                lines.add(new StatLine(
                        Component.literal(Font.SPREAD + " ")
                                .append(Component.translatable("tooltip.noitacore.spread_modification")),
                        Component.literal(String.format("%.2f°", attrs.modifications().spread()))));
            }

            if (attrs.damage() != null) {
                Font icon = attrs.damage().damageType() == DamageType.Projectile ? Font.PROJECTILE : Font.NONE;
                lines.add(new StatLine(
                        Component.literal(icon + " ")
                                .append(Component.translatable("tooltip.noitacore.damage")),
                        Component.literal(String.format("%.2f", attrs.damage().damage()))));
            }

            if (attrs.damage() != null && attrs.damage().explosion() > 0) {
                lines.add(new StatLine(
                        Component.literal(Font.EXPLOSION + " ")
                                .append(Component.translatable("tooltip.noitacore.explosion")),
                        Component.literal(String.format("%.2f", attrs.damage().explosion()))));
            }

            if (attrs.damage() != null && attrs.damage().explosionRadius() > 0) {
                lines.add(new StatLine(
                        Component.literal(Font.RADIUS + " ")
                                .append(Component.translatable("tooltip.noitacore.radius")),
                        Component.literal(String.format("%.2f", attrs.damage().explosionRadius()))));
            }

            if (attrs.motion() != null) {
                lines.add(new StatLine(
                        Component.literal(Font.SPEED + " ")
                                .append(Component.translatable("tooltip.noitacore.speed")),
                        Component.literal(String.format("%.2f", attrs.motion().initialSpeed() * 20))));
            }

            if (attrs.modifications() != null && attrs.modifications().criticalChance() != 0) {
                lines.add(new StatLine(
                        Component.literal(Font.CRIT + " ")
                                .append(Component.translatable("tooltip.noitacore.crit")),
                        Component.literal(String.format("%.2f%%", attrs.modifications().criticalChance() * 100))));
            }
        } else {
            lines.add(new StatLine(lockedComponent("spell_type"),
                    EnglishTranslationCache.getComponent(attrs.base().type().getTranslationKey()).withStyle(style -> style.withFont(ALT_FONT))));

            if (attrs.base().uses().uses() > 0 && !Caster.canSkipConsumeUses(player, attrs)) {
                Component value = lockedLiteral(String.valueOf(attrs.base().uses().uses()));
                lines.add(new StatLine(lockedComponent("tooltip.noitacore.uses"),
                        value));
            }

            lines.add(new StatLine(lockedComponent("mana_drain"),
                    lockedLiteral(String.valueOf(attrs.base().manaDrain()))));

            lines.add(new StatLine(lockedComponent("cast_delay"),
                    lockedLiteral(String.format("%.2fs", (float) attrs.base().castDelayTick() / 20))));

            lines.add(new StatLine(lockedComponent("recharge_time"),
                    lockedLiteral(String.format("%.2fs", (float) attrs.base().rechargeTick() / 20))));

            if (attrs.modifications() != null) {
                lines.add(new StatLine(lockedComponent("spread_modification"),
                        lockedLiteral(String.format("%.2f°", attrs.modifications().spread()))));
            }

            if (attrs.damage() != null) {
                lines.add(new StatLine(lockedComponent("damage"),
                        lockedLiteral(String.format("%.2f", attrs.damage().damage()))));
            }

            if (attrs.damage() != null && attrs.damage().explosion() > 0) {
                lines.add(new StatLine(lockedComponent("explosion"),
                        lockedLiteral(String.format("%.2f", attrs.damage().explosion()))));
            }

            if (attrs.damage() != null && attrs.damage().explosionRadius() > 0) {
                lines.add(new StatLine(lockedComponent("radius"),
                        lockedLiteral(String.format("%.2f", attrs.damage().explosionRadius()))));
            }

            if (attrs.motion() != null) {
                lines.add(new StatLine(lockedComponent("speed"),
                        lockedLiteral(String.format("%.2f", attrs.motion().initialSpeed() * 20))));
            }

            if (attrs.modifications() != null && attrs.modifications().criticalChance() != 0) {
                lines.add(new StatLine(lockedComponent("crit"),
                        lockedLiteral(String.format("%.2f%%", attrs.modifications().criticalChance() * 100))));
            }
        }

        return lines;
    }

    private static @NonNull MutableComponent lockedComponent(String key) {
        key = "tooltip." + MODID + "." + key;
        return EnglishTranslationCache.getComponent(key)
                .withStyle(style -> style.withFont(ALT_FONT));
    }

    private static @NonNull MutableComponent lockedLiteral(String text) {
        return Component.literal(text)
                .withStyle(style -> style.withFont(ALT_FONT));
    }
}
