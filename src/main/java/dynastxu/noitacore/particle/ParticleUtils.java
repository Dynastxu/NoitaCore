package dynastxu.noitacore.particle;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class ParticleUtils {
    public static void spawnPixelParticles(Level level, Vec3 from, @NonNull Vec3 to, double interval, double speedRange, float size, int color, int minLifeTime, int maxLifeTime) {
        Vec3 direction = to.subtract(from);
        double totalDistance = direction.length();
        Vec3 dirNormalized = direction.normalize();

        Vec3 perpDir;
        if (Math.abs(dirNormalized.x) > 0.001 || Math.abs(dirNormalized.z) > 0.001) {
            perpDir = new Vec3(-dirNormalized.z, 0, dirNormalized.x).normalize();
        } else {
            perpDir = new Vec3(1, 0, 0);
        }

        int particleCount = (int) (totalDistance / interval);

        for (int i = 1; i <= particleCount; i++) {
            double t = i * interval;
            Vec3 pointOnLine = from.add(dirNormalized.scale(t));

            double speed = (level.getRandom().nextDouble() - 0.5) * speedRange;
            Vec3 velocity = perpDir.scale(speed);

            level.addParticle(
                    new PixelParticleOptions(color, level.getRandom().nextInt(minLifeTime, maxLifeTime), size),
                    pointOnLine.x, pointOnLine.y, pointOnLine.z,
                    velocity.x, velocity.y, velocity.z
            );
        }
    }
}
