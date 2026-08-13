package dynastxu.noitacore.entity.projectile;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Nuke extends SpellProjectile{
    public Nuke(EntityType<? extends SpellProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void applyExplosion() {
        level().playSound(null, position().x, position().y, position().z, SoundEvents.GENERIC_EXPLODE, SoundSource.AMBIENT, explosionRadius, 1);
        super.applyExplosion();
    }
}
