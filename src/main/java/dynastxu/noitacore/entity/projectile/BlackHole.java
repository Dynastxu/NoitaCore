package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.common.spell.SuffixType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Comparator;

public class BlackHole extends SpellProjectile{
    public BlackHole(EntityType<? extends BlackHole> type, Level level) {
        super(type, level);
    }

    @Override
    protected void onHitBlock(@NonNull BlockHitResult hitResult) {
        castSuffixes(SuffixType.Trigger);
    }

    @Override
    public void tick() {
        if (!level().isClientSide()) {
            BlockPos center = BlockPos.containing(position());
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPos pos = center.offset(x, y, z);
                        BlockState state = level().getBlockState(pos);
                        if (state.getDestroySpeed(level(), pos) >= 0) {
                            level().destroyBlock(pos, false);
                        }
                        if (!level().getFluidState(pos).isEmpty()) {
                            level().setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                        }
                    }
                }
            }

            AABB area = AABB.ofSize(position(), 3, 3, 3);
            for (FallingBlockEntity fallingBlock : level().getEntitiesOfClass(FallingBlockEntity.class, area)) {
                fallingBlock.discard();
            }

            double maxDistance = 10.0;
            double baseStrength = 5;
            double maxAcceleration = 0.5;
            double maxVelocity = 3.0;
            double dampingZone = 5;
            AABB attractArea = AABB.ofSize(position(), maxDistance * 2, maxDistance * 2, maxDistance * 2);
            Vec3 blackHolePos = position();
            for (Entity entity : level().getEntitiesOfClass(Entity.class, attractArea, e -> e != this)) {
                double distance = entity.position().distanceTo(blackHolePos);
                if (distance > maxDistance || distance < 0.01) continue;
                double factor = 1.0 - distance / maxDistance;
                double strength = Math.min(baseStrength * factor * factor, maxAcceleration);
                Vec3 direction = blackHolePos.subtract(entity.position()).normalize();
                Vec3 velocity = entity.getDeltaMovement().add(direction.scale(strength));
                if (distance < dampingZone) {
                    double damping = 1.0 - 0.6 * (1.0 - distance / dampingZone);
                    velocity = velocity.scale(damping);
                }
                if (velocity.length() > maxVelocity) {
                    velocity = velocity.normalize().scale(maxVelocity);
                }
                entity.setDeltaMovement(velocity);
            }
        }
        super.tick();
    }

    @Override
    protected void stepMoveAndHit(BlockHitResult blockHitResult, Vec3 movementEnd) {
        while (this.isAlive()) {
            Vec3 initialPosition = this.position();
            ArrayList<EntityHitResult> entitiesHit = new ArrayList<>(this.findHitEntities(initialPosition, movementEnd));
            entitiesHit.sort(Comparator.comparingDouble(c -> initialPosition.distanceToSqr(c.getEntity().position())));

            EntityHitResult firstEntityHit = entitiesHit.isEmpty() ? null : entitiesHit.getFirst();

            if (entitiesHit.isEmpty()) {
                this.setPos(movementEnd);
                if (this.isAlive() && blockHitResult.getType() != HitResult.Type.MISS) {
                    this.onHitBlock(blockHitResult);
                    this.needsSync = true;
                }
                break;
            }

            Vec3 nextLocation = firstEntityHit.getLocation();
            this.setPos(nextLocation);
            if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
                this.handlePortal();
            }

            if (this.isAlive() && !this.noPhysics && firstEntityHit.getType() != HitResult.Type.MISS) {
                if (EventHooks.onProjectileImpact(this, firstEntityHit))
                    break;
                ProjectileDeflection deflection = this.hitTargetsOrDeflectSelf(entitiesHit);
                this.needsSync = true;
                if ((this.isPenetrating || this.isPiercing) && deflection == ProjectileDeflection.NONE) {
                    continue;
                }
                break;
            }
        }
    }
}
