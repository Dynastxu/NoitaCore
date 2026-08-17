package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.DamageTypes;
import dynastxu.noitacore.common.spell.SuffixType;
import dynastxu.noitacore.particle.ParticleUtils;
import dynastxu.noitacore.particle.pixel.PixelParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Comparator;

public class LaserEmitter extends SpellProjectile {
    protected Vec3 lastDirection = Vec3.ZERO;

    public LaserEmitter(EntityType<? extends LaserEmitter> type, Level level) {
        super(type, level);
    }

    @Override
    protected void onHitBlock(@NonNull BlockHitResult hitResult) {
        bounce(hitResult, 0.1f);
        castSuffixes(SuffixType.Trigger);
    }

    @Override
    public void tick() {
        if (!lastDirection.equals(Vec3.ZERO)) {
            Vec3 start = position();
            Vec3 direction = lastDirection;
            Vec3 end = start.add(direction.scale(16));

            if (!level().isClientSide() && level() instanceof ServerLevel serverLevel) {
                BlockHitResult blockHit = level().clipIncludingBorder(
                        new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)
                );

                Collection<EntityHitResult> entityHits = ProjectileUtil.getManyEntityHitResult(
                        level(), this, start, end,
                        getBoundingBox().expandTowards(direction.scale(16)).inflate(1.0),
                        (e) -> canHitEntity(e) || e == getOwner(), false
                );

                EntityHitResult closestEntityHit = entityHits.isEmpty() ? null : entityHits.stream()
                                                                                 .min(Comparator.comparingDouble(e -> start.distanceToSqr(e.getLocation())))
                                                                                 .orElse(null);

                double blockDist = blockHit.getType() != HitResult.Type.MISS
                        ? start.distanceToSqr(blockHit.getLocation())
                        : Double.MAX_VALUE;
                double entityDist = closestEntityHit != null
                        ? start.distanceToSqr(closestEntityHit.getLocation())
                        : Double.MAX_VALUE;

                if (blockDist < entityDist && blockHit.getType() != HitResult.Type.MISS) {
                    BlockPos pos = blockHit.getBlockPos();
                    if (level().getBlockState(pos).getDestroySpeed(level(), pos) >= 0) {
                        level().destroyBlock(pos, false);
                    }
                } else if (closestEntityHit != null) {
                    Entity entity = closestEntityHit.getEntity();
                    DamageSource damageSource = serverLevel.damageSources().source(
                            DamageTypes.SPELL_PROJECTILE, this, getOwner()
                    );
                    entity.hurtServer(serverLevel, damageSource, 1.8f);
                    entity.invulnerableTime = 0;
                }
            }

            for (int i = 0; i < 10; i++) {
                RandomSource random = level().getRandom();
                double offsetX = random.nextDouble() * 0.25;
                double offsetY = random.nextDouble() * 0.25;
                double offsetZ = random.nextDouble() * 0.25;
                if (level().isClientSide()) {
                    ParticleUtils.spawnParticles(level(),
                            position().add(offsetX, offsetY, offsetZ).add(direction),
                            position().add(offsetX, offsetY, offsetZ).add(direction.scale(16 * (0.5 + random.nextDouble() / (1 + (offsetX + offsetY + offsetZ))))),
                            0.05 + random.nextDouble() * 0.1,
                            () -> {
                                float t = random.nextFloat();
                                int r = (int) (0xFF + t * (0xCA - 0xFF));
                                int g = (int) (0xFF + t * (0xEF - 0xFF));
                                int b = (int) (0xFF + t * (0xFD - 0xFF));
                                int color = 0xFF000000 | (r << 16) | (g << 8) | b;
                                return new PixelParticleOptions(color, random.nextIntBetweenInclusive(1, 3), 0.01f);
                            });
                }
            }
        }
        lastDirection = getDeltaMovement().normalize();
        super.tick();
    }
}
