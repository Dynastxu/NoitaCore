package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.particle.ParticleUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LightBullet extends SpellProjectile {
    private final ParticleUtils.Ribbon ribbon;

    public LightBullet(EntityType<? extends LightBullet> type, Level level) {
        super(type, level);
        if (this.level().isClientSide()) {
            this.ribbon = new ParticleUtils.Ribbon(this.level(), 1d / 7, 0.2, 0.03f, 0x80A476EA, 5, 10, 64, 4);
        } else {
            this.ribbon = null;
        }
    }

    @Override
    protected void spawnStepMoveParticle(Vec3 lastPosition) {
        super.spawnStepMoveParticle(lastPosition);
        if (this.level().isClientSide()) {
            this.ribbon.spawnPixelRibbon(lastPosition, this.position());
        }
    }
}
