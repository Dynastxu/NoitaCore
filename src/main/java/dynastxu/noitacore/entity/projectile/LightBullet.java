package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.particle.ParticleUtils;
import dynastxu.noitacore.utils.SmoothVectorField;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LightBullet extends SpellProjectile {
    private final SmoothVectorField smoothVectorField;

    public LightBullet(EntityType<? extends LightBullet> type, Level level) {
        super(type, level);
        long seedA = level.getRandom().nextLong();
        long seedB = level.getRandom().nextLong();
        long seedC = level.getRandom().nextLong();
        smoothVectorField = new SmoothVectorField(seedA, seedB, seedC);
    }

    @Override
    protected void spawnStepMoveParticle(Vec3 lastPosition) {
        super.spawnStepMoveParticle(lastPosition);
        ParticleUtils.spawnFieldParticles(
                this.level(),
                lastPosition,
                this.position(),
                0.2,
                0x80A476EA,
                1, 9,
                3,
                0.5,
                new Vec3(getDeltaMovement().x * 0.1, getDeltaMovement().y * 0.1, getDeltaMovement().z * 0.1),
                smoothVectorField
        );
    }
}
