package dynastxu.noitacore.client.gui;

import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.components.SpellData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jspecify.annotations.Nullable;

public record SpellTooltipComponent(
        SpellAttributes attributes, @Nullable SpellData spellData, Player player
) implements TooltipComponent {
}
