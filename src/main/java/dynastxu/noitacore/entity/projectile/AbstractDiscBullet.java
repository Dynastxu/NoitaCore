package dynastxu.noitacore.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class AbstractDiscBullet extends SpellProjectile {

    public AbstractDiscBullet(EntityType<? extends AbstractDiscBullet> type, Level level) {
        super(type, level);
    }

    @Override
    protected boolean onWillDiscard() {
        boolean discard = super.onWillDiscard();
        if (discard) {
            spawnCorpse();
        }
        return discard;
    }

    protected abstract void spawnCorpse();
}
