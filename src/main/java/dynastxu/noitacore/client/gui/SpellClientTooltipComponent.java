package dynastxu.noitacore.client.gui;

import dynastxu.noitacore.client.font.Font;
import dynastxu.noitacore.common.spell.DamageType;
import dynastxu.noitacore.common.wand.Caster;
import dynastxu.noitacore.components.SpellData;
import dynastxu.noitacore.datamap.SpellAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SpellClientTooltipComponent implements ClientTooltipComponent {
    private static final int LABEL_VALUE_GAP = 8;

    private record StatLine(Component label, Component value) {}

    private final List<StatLine> statLines;
    private final int maxLabelWidth;
    private final int maxValueWidth;

    public SpellClientTooltipComponent(@NonNull SpellTooltipComponent component) {
        SpellAttributes attrs = component.attributes();
        SpellData spellData = component.spellData();

        this.statLines = buildStatLines(attrs, spellData, component.player());

        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        this.maxLabelWidth = statLines.stream()
                .mapToInt(sl -> mc.font.width(sl.label))
                .max()
                .orElse(0);
        this.maxValueWidth = statLines.stream()
                .mapToInt(sl -> mc.font.width(sl.value))
                .max()
                .orElse(0);
    }

    private static @NonNull List<StatLine> buildStatLines(@NonNull SpellAttributes attrs, SpellData spellData, Player player) {
        List<StatLine> lines = new ArrayList<>();

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

        return lines;
    }

    @Override
    public int getHeight(net.minecraft.client.gui.@NonNull Font font) {
        return statLines.size() * (font.lineHeight + 1);
    }

    @Override
    public int getWidth(net.minecraft.client.gui.@NonNull Font font) {
        return maxLabelWidth + LABEL_VALUE_GAP + maxValueWidth;
    }

    @Override
    public void extractText(@NonNull GuiGraphicsExtractor graphics, net.minecraft.client.gui.@NonNull Font font, int x, int y) {
        int lineHeight = font.lineHeight + 1;
        int valueX = x + maxLabelWidth + LABEL_VALUE_GAP;
        for (StatLine sl : statLines) {
            graphics.text(font, sl.label, x, y, 0xFFFFFFFF);
            graphics.text(font, sl.value, valueX, y, 0xFFFFFFFF);
            y += lineHeight;
        }
    }
}
