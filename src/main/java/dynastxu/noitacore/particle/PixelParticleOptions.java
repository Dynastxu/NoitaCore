package dynastxu.noitacore.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

public class PixelParticleOptions implements ParticleOptions {
    public static final MapCodec<PixelParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("color").forGetter(o -> o.color),
            Codec.INT.fieldOf("lifeTick").forGetter(o -> o.lifeTime),
            Codec.FLOAT.optionalFieldOf("size", 0.1f).forGetter(o -> o.size)
    ).apply(instance, PixelParticleOptions::new));

    public static final StreamCodec<ByteBuf, PixelParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, o -> o.color,
            ByteBufCodecs.VAR_INT, o -> o.lifeTime,
            ByteBufCodecs.FLOAT, o -> o.size,
            PixelParticleOptions::new
    );

    public int color;
    public int lifeTime;
    public float size;

    public PixelParticleOptions(int color, int lifeTime) {
        this(color, lifeTime, 0.1f);
    }

    public PixelParticleOptions(int color, int lifeTime, float size) {
        this.color = color;
        this.lifeTime = lifeTime;
        this.size = size;
    }

    @Override
    public @NonNull ParticleType<PixelParticleOptions> getType() {
        return ParticleTypes.PIXEL_PARTICLE.get();
    }
}
