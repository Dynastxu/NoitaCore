package dynastxu.noitacore.client.gui;

import dynastxu.noitacore.datamap.MaterialStats;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record MaterialStatsTooltipComponent(
        MaterialStats stats
) implements TooltipComponent {
}
