package dynastxu.noitacore.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class DiscBullet extends SpellProjectile{
    public DiscBullet(EntityType<? extends SpellProjectile> type, Level level) {
        super(type, level);
    }
}
