package dynastxu.noitacore.common.wand;

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
}
