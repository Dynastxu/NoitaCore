package dynastxu.noitacore.particle.explosion;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ExplosionParticleProvider implements ParticleProvider<ExplosionParticleOptions> {
    protected final SpriteSet spriteSet;

    public ExplosionParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public @Nullable Particle createParticle(@NonNull ExplosionParticleOptions options, @NonNull ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, @NonNull RandomSource random) {
        return new ExplosionParticle(level, x, y, z, spriteSet, options.size);
    }
}
