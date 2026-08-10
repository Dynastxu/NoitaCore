package dynastxu.noitacore;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class DamageTypes {
    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
            DeferredRegister.create(Registries.DAMAGE_TYPE, NoitaCore.MODID);

    public static final ResourceKey<DamageType> SPELL_PROJECTILE =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    Identifier.fromNamespaceAndPath(NoitaCore.MODID, "spell_projectile"));
}
