package dynastxu.noitacore.particle;

import dynastxu.noitacore.particle.pixel.PixelParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ParticleUtils {
    public static void spawnPixelParticles(Level level, Vec3 from, @NonNull Vec3 to, double interval, double speedRange, float size, int color, int minLifeTime, int maxLifeTime) {
        Vec3 direction = to.subtract(from);
        double totalDistance = direction.length();
        if (totalDistance < 1e-6) {
            return;
        }
        Vec3 dirNormalized = direction.normalize();

        int particleCount = (int) (totalDistance / interval);

        for (int i = 1; i <= particleCount; i++) {
            double t = i * interval;
            Vec3 pointOnLine = from.add(dirNormalized.scale(t));

            Vec3 randomVec = new Vec3(
                    level.getRandom().nextDouble() - 0.5,
                    level.getRandom().nextDouble() - 0.5,
                    level.getRandom().nextDouble() - 0.5
            );
            Vec3 perpDir = randomVec.subtract(dirNormalized.scale(randomVec.dot(dirNormalized)));
            double perpLen = perpDir.length();
            if (perpLen > 1e-12) {
                perpDir = perpDir.scale(1.0 / perpLen);
            }

            double speed = (level.getRandom().nextDouble() - 0.5) * speedRange;

            var options = new PixelParticleOptions(color, level.getRandom().nextInt(minLifeTime, maxLifeTime), size);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(options, pointOnLine.x, pointOnLine.y, pointOnLine.z, 1, 0, 0, 0, Math.abs(speed));
            } else {
                Vec3 velocity = perpDir.scale(speed);
                level.addParticle(options, pointOnLine.x, pointOnLine.y, pointOnLine.z, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    public static class Ribbon {
        protected final Level level;
        protected final double interval;
        protected final double speedRange;
        protected final float size;
        protected final int color;
        protected final int minLifeTime;
        protected final int maxLifeTime;
        protected final double rotationPeriod;
        protected final double offsetPeriod;
        protected final double initialAngle;
        @Nullable
        protected Vec3 perpDir;

        public Ribbon(@NonNull Level level, double interval, double speedRange, float size, int color, int minLifeTime, int maxLifeTime, double rotationPeriod, double offsetPeriod) {
            if (minLifeTime > maxLifeTime) {
                throw new IllegalArgumentException("minLifeTime must be less than or equal to maxLifeTime");
            }
            this.level = level;
            this.interval = interval;
            this.speedRange = speedRange;
            this.size = size;
            this.color = color;
            this.minLifeTime = minLifeTime;
            this.maxLifeTime = maxLifeTime;
            this.rotationPeriod = rotationPeriod;
            this.offsetPeriod = offsetPeriod;
            this.initialAngle = level.getRandom().nextDouble() * 2 * Math.PI;
        }

        public void spawnPixelRibbon(@NonNull Vec3 from, @NonNull Vec3 to) {
            Vec3 direction = to.subtract(from);
            double totalDistance = direction.length();
            if (totalDistance < 1e-6) {
                return;
            }
            Vec3 dirNormalized = direction.normalize();

            if (this.perpDir != null) {
                perpDir = this.perpDir.subtract(dirNormalized.scale(this.perpDir.dot(dirNormalized)));
                double len = perpDir.length();
                if (len < 1e-6) {
                    perpDir = dirNormalized.cross(new Vec3(0, 1, 0));
                    len = perpDir.length();
                    if (len < 1e-6) {
                        perpDir = new Vec3(1, 0, 0);
                    } else {
                        perpDir = perpDir.scale(1.0 / len);
                    }
                } else {
                    perpDir = perpDir.scale(1.0 / len);
                }
            } else {
                perpDir = dirNormalized.cross(new Vec3(0, 1, 0));
                double perpLen = perpDir.length();
                if (perpLen < 1e-6) {
                    perpDir = new Vec3(1, 0, 0);
                } else {
                    perpDir = perpDir.scale(1.0 / perpLen);
                }
            }
            Vec3 secondPerp = perpDir.cross(dirNormalized);

            if (Math.abs(initialAngle) > 1e-12) {
                double cosA = Math.cos(initialAngle);
                double sinA = Math.sin(initialAngle);
                Vec3 newPerp = perpDir.scale(cosA).add(secondPerp.scale(sinA));
                Vec3 newSecond = secondPerp.scale(cosA).subtract(perpDir.scale(sinA));
                perpDir = newPerp;
                secondPerp = newSecond;
            }

            int particleCount = (int) (totalDistance / interval);

            for (int i = 0; i <= particleCount; i++) {
                double t = Math.min(i * interval, totalDistance);
                Vec3 pointOnLine = from.add(dirNormalized.scale(t));

                double angle = pointOnLine.length() / rotationPeriod * 2 * Math.PI;
                Vec3 rotatedPerp = perpDir.scale(Math.cos(angle)).add(secondPerp.scale(Math.sin(angle)));

                double offsetPhase = pointOnLine.length() / offsetPeriod * Math.PI;
                double sinVal = Math.sin(offsetPhase);
                double offsetAmount = sinVal * sinVal * speedRange * 0.5;
                double speed = speedRange * sinVal * sinVal;

                Vec3 velocity = rotatedPerp.scale(speed);
                Vec3 offset = rotatedPerp.scale(offsetAmount);

                int lifeTime = maxLifeTime == minLifeTime ? minLifeTime : level.getRandom().nextInt(minLifeTime, maxLifeTime);
                var options = new PixelParticleOptions(color, lifeTime, size);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(options, pointOnLine.x + offset.x, pointOnLine.y + offset.y, pointOnLine.z + offset.z, 1, 0, 0, 0, speed);
                } else {
                    level.addParticle(options, pointOnLine.x + offset.x, pointOnLine.y + offset.y, pointOnLine.z + offset.z, velocity.x, velocity.y, velocity.z);
                }
            }
        }
    }
}
