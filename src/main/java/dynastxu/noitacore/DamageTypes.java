package dynastxu.noitacore;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class DamageTypes {
    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
            DeferredRegister.create(Registries.DAMAGE_TYPE, MODID);

    public static final ResourceKey<DamageType> SPELL_PROJECTILE =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    Identifier.fromNamespaceAndPath(MODID, "spell_projectile"));

    public static final ResourceKey<DamageType> SPELL_SLICE =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    Identifier.fromNamespaceAndPath(MODID, "spell_slice"));
}
