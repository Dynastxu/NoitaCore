package dynastxu.noitacore.particle.pixel;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

public class PixelParticleType extends ParticleType<PixelParticleOptions> {
    public PixelParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public @NonNull MapCodec<PixelParticleOptions> codec() {
        return PixelParticleOptions.CODEC;
    }

    @Override
    public @NonNull StreamCodec<ByteBuf, PixelParticleOptions> streamCodec() {
        return PixelParticleOptions.STREAM_CODEC;
    }
}
