package dynastxu.noitacore.item;

import dynastxu.noitacore.common.spell.OrbitTickManager;
import dynastxu.noitacore.common.spell.TickManager;
import dynastxu.noitacore.entity.EntityTypes;
import dynastxu.noitacore.entity.projectile.LaserEmitter;
import dynastxu.noitacore.entity.projectile.SpellProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class OrbitLasersSpellItem extends SpellItem.Modifier {
    public OrbitLasersSpellItem(@NonNull Properties properties) {
        super(properties);
    }

    @Override
    public <E extends SpellProjectile> TickManager<E> getTickManager(E entity) {
        return new OrbitLasersTickManager<>(entity);
    }

    private static class OrbitLasersTickManager<T extends SpellProjectile> extends OrbitTickManager<T, LaserEmitter> {
        public OrbitLasersTickManager(T target) {
            super(target);
        }

        @Override
        protected LaserEmitter createOrbit(Level level, Vec3 center, Vec3 offset, Entity owner) {
            LaserEmitter laser = EntityTypes.LASER_EMITTER.get().create(level, EntitySpawnReason.TRIGGERED);
            if (laser != null) {
                Vec3 direction = offset.normalize();
                laser.setPos(center.add(offset));
                laser.setOwner(owner);
                laser.setSpellItem(Items.SPELL_LASER_EMITTER);
                laser.setLaserDirection(direction);
                laser.setDeltaMovement(direction.scale(0.001));
                laser.setBreakBlocks(false);
                laser.setFriendlyFire(true);
                laser.setUpdateDirection(false);
                laser.setRemainingLifeTick(1);
                level.addFreshEntity(laser);
            }
            return laser;
        }

        @Override
        protected @NonNull LaserEmitter updateOrbit(LaserEmitter laser, Vec3 center, Vec3 offset) {
            if (laser == null) {
                laser = createOrbit(target.level(), center, offset, target.getOwner());
            }

            laser.setRemainingLifeTick(1);

            Vec3 rotatedOffset = rotate(offset, rotAxis, angle);
            Vec3 newPos = center.add(rotatedOffset);
            Vec3 direction = rotatedOffset.normalize();
            laser.setPos(newPos);
            laser.setLaserDirection(direction);
            laser.setDeltaMovement(direction.scale(0.001));
            return laser;
        }
    }
}
