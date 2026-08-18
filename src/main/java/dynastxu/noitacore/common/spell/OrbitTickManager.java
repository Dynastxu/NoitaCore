package dynastxu.noitacore.common.spell;

import dynastxu.noitacore.entity.projectile.SpellProjectile;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public abstract class OrbitTickManager<T extends SpellProjectile, Orbit extends SpellProjectile> extends TickManager<T> {
    protected Orbit orbitUp;
    protected Orbit orbitDown;
    protected Orbit orbitNorth;
    protected Orbit orbitWest;
    protected Orbit orbitEast;
    protected Orbit orbitSouth;
    protected Vec3 rotAxis;
    protected float angle;
    protected float anglePerTick = (float) (Math.PI / 20);
    protected int maxEffectiveTicks = 2400;


    public OrbitTickManager(T target) {
        super(target);

        Level level = target.level();

        RandomSource random = target.level().getRandom();
        this.rotAxis = new Vec3(
                random.nextDouble() * 2 - 1,
                random.nextDouble() * 2 - 1,
                random.nextDouble() * 2 - 1
        ).normalize();
        this.angle = 0;

        if (!level.isClientSide()) {
            Vec3 center = target.center();
            Entity owner = target.getOwner();

            orbitUp = createOrbit(level, center, new Vec3(0, 1, 0), owner);
            orbitDown = createOrbit(level, center, new Vec3(0, -1, 0), owner);
            orbitNorth = createOrbit(level, center, new Vec3(0, 0, -1), owner);
            orbitSouth = createOrbit(level, center, new Vec3(0, 0, 1), owner);
            orbitEast = createOrbit(level, center, new Vec3(1, 0, 0), owner);
            orbitWest = createOrbit(level, center, new Vec3(-1, 0, 0), owner);
        }
    }

    protected static @NonNull Vec3 rotate(Vec3 v, @NonNull Vec3 axis, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double dot = axis.dot(v);
        Vec3 cross = axis.cross(v);
        return new Vec3(
                v.x * cos + cross.x * sin + axis.x * dot * (1 - cos),
                v.y * cos + cross.y * sin + axis.y * dot * (1 - cos),
                v.z * cos + cross.z * sin + axis.z * dot * (1 - cos)
        );
    }

    @Override
    public void onTick() {
        if (target.level().isClientSide()) {
            return;
        }

        if (target.getLivedTick() > maxEffectiveTicks) {
            return;
        }

        if (target == null || !target.isAlive()) {
            return;
        }

        angle += anglePerTick;
        if (angle >= Math.PI * 2) {
            angle -= (float) (Math.PI * 2);
        }

        Vec3 center = target.center();

        orbitUp = updateOrbit(orbitUp, center, new Vec3(0, 1, 0));
        orbitDown = updateOrbit(orbitDown, center, new Vec3(0, -1, 0));
        orbitNorth = updateOrbit(orbitNorth, center, new Vec3(0, 0, -1));
        orbitSouth = updateOrbit(orbitSouth, center, new Vec3(0, 0, 1));
        orbitEast = updateOrbit(orbitEast, center, new Vec3(1, 0, 0));
        orbitWest = updateOrbit(orbitWest, center, new Vec3(-1, 0, 0));
    }

    protected abstract Orbit createOrbit(Level level, Vec3 center, Vec3 offset, Entity owner);
    protected abstract Orbit updateOrbit(Orbit projectile, Vec3 center, Vec3 offset);
}
