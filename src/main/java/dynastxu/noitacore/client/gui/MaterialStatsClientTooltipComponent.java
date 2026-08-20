package dynastxu.noitacore.client.gui;

import dynastxu.noitacore.datamap.MaterialStats;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MaterialStatsClientTooltipComponent extends AbstractStatClientTooltipComponent {

    public MaterialStatsClientTooltipComponent(@NonNull MaterialStatsTooltipComponent component) {
        super(buildStatLines(component.stats()));
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
}
