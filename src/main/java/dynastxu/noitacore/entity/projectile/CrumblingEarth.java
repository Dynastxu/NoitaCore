package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.world.level.explosion.Earthquake;
import dynastxu.noitacore.world.level.explosion.ExplosionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class CrumblingEarth extends SpellProjectile {

    public CrumblingEarth(EntityType<? extends SpellProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void onWillDiscard() {
        if (level() instanceof ServerLevel serverLevel) {
            LivingEntity indirectSourceEntity = getOwner() instanceof LivingEntity livingEntity ? livingEntity : null;
            Earthquake earthquake = new Earthquake(serverLevel, this, indirectSourceEntity, position(), 64);
            ExplosionManager.add(serverLevel, earthquake);
        }

        super.onWillDiscard();
    }
}
