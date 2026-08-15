package dynastxu.noitacore.entity;

import dynastxu.noitacore.entity.projectile.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class EntityTypes {
    private static final int SPELL_PROJECTILE_CLIENT_TRACKING_RANGE = 4;
    private static final int SPELL_PROJECTILE_UPDATE_INTERVAL = 10;

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    public static final Supplier<EntityType<RubberBall>> RUBBER_BALL =
            registerSpellProjectile("rubber_ball", 0.25f, RubberBall::new);

    public static final Supplier<EntityType<LightBullet>> LIGHT_BULLET =
            registerSpellProjectile("light_bullet", 1f/7, LightBullet::new);

    public static final Supplier<EntityType<Nuke>> NUKE =
            registerSpellProjectile("nuke", 0.5f, Nuke::new);

    public static final Supplier<EntityType<CrumblingEarth>> CRUMBLING_EARTH =
            registerSpellProjectile("crumbling_earth", 1f/7, CrumblingEarth::new);

    public static final Supplier<EntityType<Funky>> FUNKY =
            registerSpellProjectile("funky", 1f/7, Funky::new);

    public static final Supplier<EntityType<BlackHole>> BLACK_HOLE =
            registerSpellProjectile("black_hole", 3f, BlackHole::new);

    public static final Supplier<EntityType<Buckshot>> BUCKSHOT =
            registerSpellProjectile("buckshot", 2f/7, Buckshot::new);

    private static <T extends SpellProjectile> @NonNull Supplier<EntityType<T>> registerSpellProjectile(String name, float size, EntityType.EntityFactory<T> factory) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder
                .of(factory, MobCategory.MISC)
                .sized(size, size)
                .clientTrackingRange(SPELL_PROJECTILE_CLIENT_TRACKING_RANGE)
                .updateInterval(SPELL_PROJECTILE_UPDATE_INTERVAL)
                .build(ResourceKey.create(Registries.ENTITY_TYPE,
                        Identifier.fromNamespaceAndPath(MODID, name))));
    }
}
