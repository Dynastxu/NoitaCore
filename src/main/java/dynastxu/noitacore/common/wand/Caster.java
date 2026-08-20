package dynastxu.noitacore.common.wand;

import dynastxu.noitacore.datamap.SpellAttributes;
import net.minecraft.world.entity.player.Player;

public record Caster<T>(
        T caster
) {
    public boolean canSkipConsumeUses() {
        if (caster instanceof Player player) {
            return false;
        }
        return true;
    }

    public static boolean canSkipConsumeUses(Player player) {
        return new Caster<>(player).canSkipConsumeUses();
    }

    public static boolean canSkipConsumeUses(Player player, SpellAttributes attributes) {
        return canSkipConsumeUses(player) && !attributes.base().uses().mustConsume();
    }
}
