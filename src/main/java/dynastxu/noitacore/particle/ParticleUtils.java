package dynastxu.noitacore.particle;

import dynastxu.noitacore.particle.pixel.PixelParticleOptions;
import dynastxu.noitacore.utils.SmoothVectorField;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public class ParticleUtils {
    public static void spawnPixelParticles(Level level, Vec3 from, @NonNull Vec3 to, double interval, double speedRange, float size, int color, int minLifeTime, int maxLifeTime) {
        spawnParticles(
                level, from, to, interval,
                () -> new PixelParticleOptions(color, level.getRandom().nextIntBetweenInclusive(minLifeTime, maxLifeTime), size),
                () -> (level.getRandom().nextDouble() - 0.5) * speedRange);
    }

    public static void spawnParticles(Level level, Vec3 from, @NonNull Vec3 to, double interval, Supplier<ParticleOptions> options) {
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

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(options.get(), pointOnLine.x, pointOnLine.y, pointOnLine.z, 1, 0, 0, 0, 0);
            } else {
                level.addParticle(options.get(), pointOnLine.x, pointOnLine.y, pointOnLine.z, 0, 0, 0);
            }
        }
    }

    public static void spawnParticles(Level level, Vec3 from, @NonNull Vec3 to, double interval, Supplier<ParticleOptions> options, Supplier<Double> speed) {
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

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(options.get(), pointOnLine.x, pointOnLine.y, pointOnLine.z, 1, 0, 0, 0, Math.abs(speed.get()));
            } else {
                Vec3 velocity = perpDir.scale(speed.get());
                level.addParticle(options.get(), pointOnLine.x, pointOnLine.y, pointOnLine.z, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    public static void spawnFieldParticles(@NonNull Level level, @NonNull Vec3 from, @NonNull Vec3 to, double interval, int color, int minLifeTime, int maxLifeTime, double scale, double maxSpeed, Vec3 additionSpeed, SmoothVectorField smoothVectorField) {
        if (!level.isClientSide()) return;
        double distance = to.distanceTo(from);
        int particleCount = (int) (distance / interval);
        double stepX = (to.x - from.x) / particleCount;
        double stepY = (to.y - from.y) / particleCount;
        double stepZ = (to.z - from.z) / particleCount;
        if (particleCount > 0) {
            double x = from.x;
            double y = from.y;
            double z = from.z;
            for (int i = 0; i <= particleCount; i++) {
                x += stepX;
                y += stepY;
                z += stepZ;
                var d = smoothVectorField.vectorAt(x, y, z, maxSpeed, scale);
                var dx = d[0] + additionSpeed.x;
                var dy = d[1] + additionSpeed.y;
                var dz = d[2] + additionSpeed.z;
                int lifeTime = minLifeTime == maxLifeTime ? minLifeTime : level.getRandom().nextIntBetweenInclusive(minLifeTime, maxLifeTime);
                level.addParticle(
                        new PixelParticleOptions(color, lifeTime),
                        x, y, z,
                        dx, dy, dz
                );
            }
        }
    }
}
