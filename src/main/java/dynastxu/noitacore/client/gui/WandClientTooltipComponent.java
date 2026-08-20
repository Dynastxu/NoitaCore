package dynastxu.noitacore.client.gui;

import dynastxu.noitacore.client.font.Font;
import dynastxu.noitacore.common.wand.WandStatistics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class WandClientTooltipComponent extends AbstractStatClientTooltipComponent {
    private static final int ICON_SIZE = 16;
    private static final int ICON_GAP = 2;
    private static final int MAX_PER_ROW = 8;
    private static final int GAP = 2;

    private final Component label;
    private final int labelWidth;
    private final List<ItemStack> alwaysCasts;
    private final int gridWidth;
    private final int gridHeight;

    public WandClientTooltipComponent(@NonNull WandTooltipComponent component) {
        super(buildStatLines(component.wandData().statistics()));

        WandStatistics stats = component.wandData().statistics();
        this.alwaysCasts = new ArrayList<>();
        for (Holder<Item> holder : stats.alwaysCasts()) {
            this.alwaysCasts.add(holder.value().getDefaultInstance());
        }

        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        this.label = Component.translatable("tooltip.noitacore.always_casts");
        this.labelWidth = alwaysCasts.isEmpty() ? 0 : mc.font.width(label);

        int cols = Math.min(alwaysCasts.size(), MAX_PER_ROW);
        this.gridWidth = alwaysCasts.isEmpty() ? 0 : cols * ICON_SIZE + (cols - 1) * ICON_GAP;

        int rows = (alwaysCasts.size() + MAX_PER_ROW - 1) / MAX_PER_ROW;
        this.gridHeight = alwaysCasts.isEmpty() ? 0 : rows * ICON_SIZE + (rows - 1) * ICON_GAP;
    }

    private static @NonNull List<StatLine> buildStatLines(@NonNull WandStatistics stats) {
        List<StatLine> lines = new ArrayList<>();

        lines.add(new StatLine(
                Component.literal(Font.SHUFFLE + " ")
                        .append(Component.translatable("tooltip.noitacore.shuffle")),
                stats.shuffle() ? Component.translatable("gui.yes") : Component.translatable("gui.no")));

        lines.add(new StatLine(
                Component.literal(Font.SPELLS_PER_CAST + " ")
                        .append(Component.translatable("tooltip.noitacore.spells_per_cast")),
                Component.literal(String.valueOf(stats.spellsPerCast()))));

        lines.add(new StatLine(
                Component.literal(Font.CAST_DELAY + " ")
                        .append(Component.translatable("tooltip.noitacore.cast_delay")),
                Component.literal(String.format("%.2fs", (float) stats.castDelayTick() / 20))));

        lines.add(new StatLine(
                Component.literal(Font.RECHARGE + " ")
                        .append(Component.translatable("tooltip.noitacore.recharge_time")),
                Component.literal(String.format("%.2fs", (float) stats.rechargeTick() / 20))));

        lines.add(new StatLine(
                Component.literal(Font.MANA + " ")
                        .append(Component.translatable("tooltip.noitacore.mana_max")),
                Component.literal(String.valueOf(stats.manaMax()))));

        lines.add(new StatLine(
                Component.literal(Font.MANA_CHARGE + " ")
                        .append(Component.translatable("tooltip.noitacore.mana_charge_speed")),
                Component.literal(String.valueOf(stats.manaChargeSpeed()))));

        lines.add(new StatLine(
                Component.literal(Font.CAPACITY + " ")
                        .append(Component.translatable("tooltip.noitacore.capacity")),
                Component.literal(String.valueOf(stats.capacity()))));

        lines.add(new StatLine(
                Component.literal(Font.SPREAD + " ")
                        .append(Component.translatable("tooltip.noitacore.spread")),
                Component.literal(String.format("%.2f°", stats.spread()))));

        lines.add(new StatLine(
                Component.literal(Font.SPEED + " ")
                        .append(Component.translatable("tooltip.noitacore.speed")),
                Component.literal(String.format("%.2f×", stats.speedMultiplier()))));

        return lines;
    }

    @Override
    public int getHeight(net.minecraft.client.gui.@NonNull Font font) {
        int textHeight = super.getHeight(font);
        if (alwaysCasts.isEmpty()) {
            return textHeight;
        }
        return (font.lineHeight + 1) + gridHeight + GAP + textHeight;
    }

    @Override
    public int getWidth(net.minecraft.client.gui.@NonNull Font font) {
        int statsWidth = super.getWidth(font);
        return Math.max(Math.max(labelWidth, gridWidth), statsWidth);
    }

    @Override
    public void extractText(@NonNull GuiGraphicsExtractor graphics, net.minecraft.client.gui.@NonNull Font font, int x, int y) {
        if (!alwaysCasts.isEmpty()) {
            graphics.text(font, label, x, y, 0xFFFFFFFF);
            y += font.lineHeight + 1 + gridHeight + GAP;
        }
        renderStatLines(graphics, font, x, y);
    }

    @Override
    public void extractImage(net.minecraft.client.gui.@NonNull Font font, int x, int y, int w, int h, @NonNull GuiGraphicsExtractor graphics) {
        if (alwaysCasts.isEmpty()) return;

        int gridY = y + font.lineHeight + 1;
        for (int i = 0; i < alwaysCasts.size(); i++) {
            int col = i % MAX_PER_ROW;
            int row = i / MAX_PER_ROW;
            int ix = x + col * (ICON_SIZE + ICON_GAP);
            int iy = gridY + row * (ICON_SIZE + ICON_GAP);
            graphics.fakeItem(alwaysCasts.get(i), ix, iy);
            graphics.itemDecorations(font, alwaysCasts.get(i), ix, iy);
        }
    }
}
