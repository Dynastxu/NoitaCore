package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.particle.ParticleUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Buckshot extends SpellProjectile {
    public Buckshot(EntityType<? extends Buckshot> type, Level level) {
        super(type, level);
        bounceStrength = 0.9f;
    }

    @Override
    protected void spawnStepMoveParticle(Vec3 lastPosition) {
        super.spawnStepMoveParticle(lastPosition);
        ParticleUtils.spawnPixelParticles(level(), lastPosition, position(), 1, 0.1, 0.0025f, 0xbddd65, 2, 4);
    }
}
