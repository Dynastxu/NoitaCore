package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.common.spell.DamageType;
import dynastxu.noitacore.particle.ParticleUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class RubberBall extends SpellProjectile {
    public RubberBall(EntityType<? extends RubberBall> type, Level level) {
        super(type, level);
    }

    @Override
    protected void spawnStepMoveParticle(Vec3 lastPosition) {
        super.spawnStepMoveParticle(lastPosition);
        ParticleUtils.spawnPixelParticles(this.level(), lastPosition, this.position(), 1d/7, 1d/7, 0.025f, 0x8000ff00, 1, 10);
    }

    @Override
    protected void onBounced(BlockHitResult hitResult) {
        super.onBounced(hitResult);
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SLIME_SQUISH, SoundSource.AMBIENT, 1.0f, 1.0f);
        }
    }

    @Override
    protected void hurtEntity(Entity entity) {
        float damage = damageMap.getOrDefault(DamageType.Projectile, 0f);
        damageMap.put(DamageType.Projectile, calculateDamageDependsOnSpeed(damage));
        super.hurtEntity(entity);
    }
}
