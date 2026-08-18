package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.particle.ParticleUtils;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TeleportProjectile extends SpellProjectile {
    public TeleportProjectile(EntityType<? extends TeleportProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void spawnStepMoveParticle(Vec3 lastPosition) {
        super.spawnStepMoveParticle(lastPosition);
        ParticleUtils.spawnParticles(level(), lastPosition, position(), 0.1f, () -> new DustParticleOptions(0xffffff, 1.0f));
    }

    @Override
    protected void onWillDiscard() {
        super.onWillDiscard();
        Entity entity = getOwner();
        if (entity != null) {
            entity.teleportTo(position().x, position().y, position().z);
        }
    }
}
