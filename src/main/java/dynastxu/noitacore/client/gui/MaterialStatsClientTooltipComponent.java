package dynastxu.noitacore.client.gui;

import dynastxu.noitacore.datamap.MaterialStats;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MaterialStatsClientTooltipComponent implements ClientTooltipComponent {
    private static final int LABEL_VALUE_GAP = 8;

    private record StatLine(Component label, Component value) {}

    private final List<StatLine> statLines;
    private final int maxLabelWidth;
    private final int maxValueWidth;

    public MaterialStatsClientTooltipComponent(@NonNull MaterialStatsTooltipComponent component) {
        MaterialStats stats = component.stats();
        Minecraft mc = Minecraft.getInstance();
        this.statLines = buildStatLines(stats);

        this.maxLabelWidth = statLines.stream()
                .mapToInt(sl -> mc.font.width(sl.label))
                .max()
                .orElse(0);
        this.maxValueWidth = statLines.stream()
                .mapToInt(sl -> mc.font.width(sl.value))
                .max()
                .orElse(0);
    }

    private static @NonNull List<StatLine> buildStatLines(@NonNull MaterialStats stats) {
        List<StatLine> lines = new ArrayList<>();

        lines.add(new StatLine(
                Component.translatable("tooltip.noitacore.durability").withStyle(ChatFormatting.GRAY),
                Component.literal(String.valueOf(stats.durability())).withStyle(ChatFormatting.GRAY)));

        lines.add(new StatLine(
                Component.translatable("tooltip.noitacore.density").withStyle(ChatFormatting.GRAY),
                Component.literal(String.valueOf(stats.density())).withStyle(ChatFormatting.GRAY)));

        lines.add(new StatLine(
                Component.translatable("tooltip.noitacore.hardness").withStyle(ChatFormatting.GRAY),
                Component.literal(String.format("%.2f", stats.hardness())).withStyle(ChatFormatting.GRAY)));

        lines.add(new StatLine(
                Component.translatable("tooltip.noitacore.conductive").withStyle(ChatFormatting.GRAY),
                Component.translatable(stats.conductive() ? "gui.yes" : "gui.no").withStyle(ChatFormatting.GRAY)));

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
