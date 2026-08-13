package dynastxu.noitacore.entity;

import dynastxu.noitacore.entity.projectile.LightBullet;
import dynastxu.noitacore.entity.projectile.Nuke;
import dynastxu.noitacore.entity.projectile.RubberBall;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class EntityTypes {
    private static final int SPELL_PROJECTILE_CLIENT_TRACKING_RANGE = 4;
    private static final int SPELL_PROJECTILE_UPDATE_INTERVAL = 10;

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    public static final Supplier<EntityType<RubberBall>> RUBBER_BALL =
            ENTITY_TYPES.register("rubber_ball", () -> EntityType.Builder
                    .of(RubberBall::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(SPELL_PROJECTILE_CLIENT_TRACKING_RANGE)
                    .updateInterval(SPELL_PROJECTILE_UPDATE_INTERVAL)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(MODID, "rubber_ball"))));

    public static final Supplier<EntityType<LightBullet>> LIGHT_BULLET =
            ENTITY_TYPES.register("light_bullet", () -> EntityType.Builder
                    .of(LightBullet::new, MobCategory.MISC)
                    .sized(1f/7, 1f/7)
                    .clientTrackingRange(SPELL_PROJECTILE_CLIENT_TRACKING_RANGE)
                    .updateInterval(SPELL_PROJECTILE_UPDATE_INTERVAL)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(MODID, "light_bullet"))));

    public static final Supplier<EntityType<Nuke>> NUKE =
            ENTITY_TYPES.register("nuke", () -> EntityType.Builder
                    .of(Nuke::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(SPELL_PROJECTILE_CLIENT_TRACKING_RANGE)
                    .updateInterval(SPELL_PROJECTILE_UPDATE_INTERVAL)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(MODID, "nuke"))));
}
