package dynastxu.noitacore.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class PixelParticleProvider implements ParticleProvider<PixelParticleOptions> {
    private final SpriteSet spriteSet;

    public PixelParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public @Nullable Particle createParticle(@NonNull PixelParticleOptions options, @NonNull ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, @NonNull RandomSource random) {
        return new PixelParticle(level, x, y, z, xAux, yAux, zAux, options.color, options.lifeTime, options.size, spriteSet);
    }
}
