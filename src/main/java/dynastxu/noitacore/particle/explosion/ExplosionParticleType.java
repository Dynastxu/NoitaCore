package dynastxu.noitacore.particle.explosion;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

public class ExplosionParticleType extends ParticleType<ExplosionParticleOptions> {
    public ExplosionParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public @NonNull MapCodec<ExplosionParticleOptions> codec() {
        return ExplosionParticleOptions.CODEC;
    }

    @Override
    public @NonNull StreamCodec<ByteBuf, ExplosionParticleOptions> streamCodec() {
        return ExplosionParticleOptions.STREAM_CODEC;
    }
}
