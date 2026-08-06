package dynastxu.noitacore.datagen;

import dynastxu.noitacore.particle.ParticleTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class ModParticleDescriptionProvider extends ParticleDescriptionProvider {
    public ModParticleDescriptionProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void addDescriptions() {
        spriteSet(ParticleTypes.PIXEL_PARTICLE.get(), Identifier.fromNamespaceAndPath(MODID, "pixel"));
    }
}
