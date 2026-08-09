package dynastxu.noitacore.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class ParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MODID);

    public static final Supplier<PixelParticleType> PIXEL_PARTICLE =
            PARTICLE_TYPES.register("pixel", () -> new PixelParticleType(true));
}
