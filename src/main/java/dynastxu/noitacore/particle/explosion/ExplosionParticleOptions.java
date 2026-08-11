package dynastxu.noitacore.particle.explosion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.particle.ParticleTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

public class ExplosionParticleOptions implements ParticleOptions {
    public static final MapCodec<ExplosionParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(o -> o.size)
    ).apply(instance, ExplosionParticleOptions::new));
    public static final StreamCodec<ByteBuf, ExplosionParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, o -> o.size,
            ExplosionParticleOptions::new
    );

    public float size;

    public ExplosionParticleOptions(float size) {
        this.size = size;
    }

    @Override
    public @NonNull ParticleType<ExplosionParticleOptions> getType() {
        return ParticleTypes.EXPLOSION_PARTICLE.get();
    }
}
