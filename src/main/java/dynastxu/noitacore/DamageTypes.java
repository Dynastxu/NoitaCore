package dynastxu.noitacore;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class DamageTypes {
    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
            DeferredRegister.create(Registries.DAMAGE_TYPE, NoitaCore.MODID);

    public static final ResourceKey<DamageType> SPELL_PROJECTILE =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    Identifier.fromNamespaceAndPath(NoitaCore.MODID, "spell_projectile"));

    public static final Supplier<DamageType> SPELL_PROJECTILE_TYPE =
            DAMAGE_TYPES.register("spell_projectile", () -> new DamageType(
                    "spell_projectile",
                    DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                    0.1F,
                    DamageEffects.HURT,
                    DeathMessageType.DEFAULT
            ));
}
