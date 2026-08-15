package dynastxu.noitacore.entity.projectile;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import dynastxu.noitacore.DamageTypes;
import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.DataSerializers;
import dynastxu.noitacore.common.spell.DamageType;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.common.spell.Suffix;
import dynastxu.noitacore.common.spell.SuffixType;
import dynastxu.noitacore.item.SpellItem;
import dynastxu.noitacore.particle.explosion.ExplosionParticleOptions;
import dynastxu.noitacore.utils.EnumCodecs;
import dynastxu.noitacore.world.level.explosion.ExplosionManager;
import dynastxu.noitacore.world.level.explosion.SpellExplosion;
import lombok.Setter;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

public abstract class SpellProjectile extends Projectile {
    protected static final EntityDataAccessor<Integer> REMAINING_BOUNCES =
            SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> REMAINING_LIFE_TICK =
            SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> SUFFIX_TIMER =
            SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> LIFE_TICK =
            SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);
    /**
     * 伤害过的实体
     */
    protected static final EntityDataAccessor<List<EntityReference<Entity>>> HURT_ENTITIES =
            SynchedEntityData.defineId(SpellProjectile.class, DataSerializers.HURT_ENTITIES_SERIALIZER.get());
    private static final Logger LOGGER = LogUtils.getLogger();
    protected Holder<Item> spellItem;
    protected List<Holder<Item>> modifiers = new ArrayList<>();
    @Setter
    protected List<Suffix> suffixes = new ArrayList<>();
    /**
     * 仅对同一实体造成一次伤害
     */
    protected boolean isPenetrating;
    /**
     * 可对同一实体重复伤害
     */
    protected boolean isPiercing;
    protected boolean isFriendlyFire;
    protected float gravity;
    protected float friction;
    protected Map<DamageType, Float> damageMap = new HashMap<>();
    protected float dieSpeedThreshold;
    protected float initialSpeed;
    protected float explosion;
    protected float explosionRadius;
    protected float diggingPower;
    protected float critChance;
    protected float bounceStrength = 0.5f;

    public SpellProjectile(EntityType<? extends SpellProjectile> type, Level level) {
        super(type, level);
    }

    public void set(@NonNull Holder<Item> spellItem, @NonNull List<Holder<Item>> modifiers, float initialSpeed, float critChance) {
        this.spellItem = spellItem;
        this.modifiers = new ArrayList<>(modifiers);
        this.initialSpeed = Math.max(initialSpeed, 0);

        InitialState state = new InitialState();
        state.critChance = critChance;

        SpellAttributes spellAttributes = getMainSpellAttributes();
        if (spellAttributes != null) {
            if (spellAttributes.damage() != null) {
                state.penetrating = spellAttributes.damage().penetrating();
                state.piercing = spellAttributes.damage().piercing();
                state.friendlyFire = spellAttributes.damage().friendlyFire();
                state.damageMap.merge(spellAttributes.damage().damageType(), spellAttributes.damage().damage(), Float::sum);
                state.explosion = spellAttributes.damage().explosion();
                state.explosionRadius = spellAttributes.damage().explosionRadius();
            }
            if (spellAttributes.motion() != null) {
                state.gravity = spellAttributes.motion().gravity();
                state.bounces = spellAttributes.motion().bounces();
                state.friction = spellAttributes.motion().friction();
                state.dieSpeedThreshold = spellAttributes.motion().dieSpeedThreshold();
            }
            if (spellAttributes.time() != null) {
                state.lifeTick = spellAttributes.time().lifeTick();
            }
            if (spellAttributes.suffix() != null) {
                state.suffixTimer = spellAttributes.suffix().timerLifeTick();
            }
            if (spellAttributes.other() != null) {
                state.diggingPower = spellAttributes.other().diggingPower();
            }
            if (spellAttributes.modifications() != null) {
                state.critChance = spellAttributes.modifications().criticalChance();
            }
        }

        for (Holder<Item> modifier : modifiers) {
            SpellAttributes modifierAttributes = modifier.getData(DataMaps.SPELL_ATTRIBUTES);
            if (modifierAttributes != null) {
                if (modifierAttributes.damage() != null) {
                    state.penetrating = state.penetrating || modifierAttributes.damage().penetrating();
                    state.piercing = state.piercing || modifierAttributes.damage().piercing();
                    state.friendlyFire = state.friendlyFire || modifierAttributes.damage().friendlyFire();
                    state.damageMap.merge(modifierAttributes.damage().damageType(), modifierAttributes.damage().damage(), Float::sum);
                    state.explosion += modifierAttributes.damage().explosion();
                    state.explosionRadius += modifierAttributes.damage().explosionRadius();
                }
                if (modifierAttributes.motion() != null) {
                    state.gravity += modifierAttributes.motion().gravity();
                    state.bounces += modifierAttributes.motion().bounces();
                    state.friction += modifierAttributes.motion().friction();
                    state.dieSpeedThreshold += modifierAttributes.motion().dieSpeedThreshold();
                }
                if (modifierAttributes.time() != null) {
                    state.lifeTick += modifierAttributes.time().lifeTick();
                }
                if (modifierAttributes.suffix() != null) {
                    state.suffixTimer += modifierAttributes.suffix().timerLifeTick();
                }
                if (modifierAttributes.other() != null) {
                    state.diggingPower += modifierAttributes.other().diggingPower();
                }
                if (modifierAttributes.modifications() != null) {
                    state.critChance += modifierAttributes.modifications().criticalChance();
                }
            }
        }

        for (Holder<Item> modifier : modifiers) {
            Item item = modifier.value();
            if (item instanceof SpellItem si) {
                si.onWillApplyInitialState(state);
            }
        }

        apply(state);
        this.needsSync = true;
    }

    protected void apply(@NonNull InitialState state) {
        state.setValid();

        this.isPiercing = state.penetrating;
        this.isPenetrating = state.penetrating;
        this.isFriendlyFire = state.friendlyFire;
        this.gravity = state.gravity;
        this.friction = state.friction;
        this.damageMap = state.damageMap;
        this.dieSpeedThreshold = state.dieSpeedThreshold;
        this.diggingPower = state.diggingPower;
        this.explosion = state.explosion;
        this.explosionRadius = state.explosionRadius;
        this.critChance = state.critChance;

        this.entityData.set(REMAINING_BOUNCES, state.bounces);
        this.entityData.set(REMAINING_LIFE_TICK, state.lifeTick);
        this.entityData.set(SUFFIX_TIMER, state.suffixTimer);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        entityData.define(REMAINING_BOUNCES, 0);
        entityData.define(REMAINING_LIFE_TICK, 0);
        entityData.define(SUFFIX_TIMER, 0);
        entityData.define(HURT_ENTITIES, new ArrayList<>());
        entityData.define(LIFE_TICK, 0);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("RemainingBounces", entityData.get(REMAINING_BOUNCES));
        output.putInt("RemainingLifeTick", entityData.get(REMAINING_LIFE_TICK));
        output.putInt("SuffixTimer", entityData.get(SUFFIX_TIMER));
        output.putFloat("Gravity", gravity);
        output.putFloat("Friction", friction);
        if (!damageMap.isEmpty()) {
            output.store("DamageMap",
                    Codec.unboundedMap(EnumCodecs.codec(DamageType.class), Codec.FLOAT),
                    damageMap);
        }
        output.putFloat("DieSpeedThreshold", dieSpeedThreshold);
        output.putFloat("InitialSpeed", initialSpeed);
        output.putFloat("Explosion", explosion);
        output.putFloat("ExplosionRadius", explosionRadius);
        output.putFloat("DiggingPower", diggingPower);
        output.putFloat("CritChance", critChance);
        output.putBoolean("IsPenetrating", isPenetrating);
        output.putBoolean("IsPiercing", isPiercing);
        output.putBoolean("IsFriendlyFire", isFriendlyFire);
        if (!entityData.get(HURT_ENTITIES).isEmpty()) {
            output.store("HurtEntities", Codec.list(EntityReference.codec()), entityData.get(HURT_ENTITIES));
        }
        if (spellItem != null) {
            output.store("SpellItem", Item.CODEC, spellItem);
        }
        if (!modifiers.isEmpty()) {
            output.store("Modifiers", Codec.list(Item.CODEC), modifiers);
        }
        if (suffixes != null && !suffixes.isEmpty()) {
            output.store("Suffixes", Codec.list(Suffix.CODEC), suffixes);
        }
        output.putInt("LifeTick", entityData.get(LIFE_TICK));
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(REMAINING_BOUNCES, input.getInt("RemainingBounces").orElse(0));
        entityData.set(REMAINING_LIFE_TICK, input.getInt("RemainingLifeTick").orElse(0));
        entityData.set(SUFFIX_TIMER, input.getInt("SuffixTimer").orElse(0));
        gravity = input.getFloatOr("Gravity", 0);
        friction = input.getFloatOr("Friction", 0);
        damageMap = new HashMap<>(input.read("DamageMap",
                        Codec.unboundedMap(EnumCodecs.codec(DamageType.class), Codec.FLOAT))
                .orElse(Map.of()));
        dieSpeedThreshold = input.getFloatOr("DieSpeedThreshold", 0);
        initialSpeed = input.getFloatOr("InitialSpeed", 0);
        explosion = input.getFloatOr("Explosion", 0);
        explosionRadius = input.getFloatOr("ExplosionRadius", 0);
        diggingPower = input.getFloatOr("DiggingPower", 0);
        critChance = input.getFloatOr("CritChance", 0);
        isPenetrating = input.getBooleanOr("IsPenetrating", false);
        isPiercing = input.getBooleanOr("IsPiercing", false);
        isFriendlyFire = input.getBooleanOr("IsFriendlyFire", false);
        //noinspection unchecked
        entityData.set(HURT_ENTITIES, (List<EntityReference<Entity>>) (Object) input.read("HurtEntities", Codec.list(EntityReference.codec())).orElse(new ArrayList<>()));
        spellItem = input.read("SpellItem", Item.CODEC).orElse(null);
        modifiers = new ArrayList<>(input.read("Modifiers", Codec.list(Item.CODEC)).orElse(List.of()));
        suffixes = new ArrayList<>(input.read("Suffixes", Codec.list(Suffix.CODEC)).orElse(List.of()));
        entityData.set(LIFE_TICK, input.getInt("LifeTick").orElse(0));
    }

    protected @Nullable SpellAttributes getMainSpellAttributes() {
        if (spellItem == null) return null;
        SpellAttributes spellAttributes = spellItem.getData(DataMaps.SPELL_ATTRIBUTES);
        if (spellAttributes == null) {
            LOGGER.error("未查找到法术属性");
        }
        return spellAttributes;
    }

    protected double getFluidHeight() {
        var fluidHeight = 0d;
        if (isInWater()) {
            fluidHeight = getFluidHeight(FluidTags.WATER);
        } else if (isInLava()) {
            fluidHeight = getFluidHeight(FluidTags.LAVA);
        }
        return fluidHeight;
    }

    protected void applyPhysics() {
        var g = -gravity / 2800;
        var fl = 0d; // TODO 计算浮力，需要流体密度
        var fax = 0d;
        var fay = 0d;
        var faz = 0d;
        if (isInLiquid()) {
            var fluidHeight = getFluidHeight();
            fax = -(friction / 10.5) * getDeltaMovement().x * getDeltaMovement().x;
            fay = -(friction / 10.5) * getDeltaMovement().y * getDeltaMovement().y;
            faz = -(friction / 10.5) * getDeltaMovement().z * getDeltaMovement().z;
        } else {
            fax = -(friction / 8400) * getDeltaMovement().x;
            fay = -(friction / 8400) * getDeltaMovement().y;
            faz = -(friction / 8400) * getDeltaMovement().z;
        }
        setDeltaMovement(
                getDeltaMovement().x + fax,
                getDeltaMovement().y + fay + g + fl,
                getDeltaMovement().z + faz
        );
    }

    protected void spawnStepMoveParticle(Vec3 lastPosition) {
        // FIXME 服务端销毁实体后，客户端无法渲染最后一刻粒子效果 在服务端计算粒子会消耗性能，需要其他解决方案
    }

    @Override
    public void tick() {
        entityData.set(LIFE_TICK, entityData.get(LIFE_TICK) + 1);

        if (dieSpeedThreshold > 0) {
            if (getDeltaMovement().length() <= dieSpeedThreshold) {
                onWillDiscard();
                discard();
            }
        }

        Vec3 movement = this.getDeltaMovement();
        Vec3 originalPosition = this.position();
        BlockHitResult blockHitResult = this.level()
                .clipIncludingBorder(
                        new ClipContext(originalPosition, originalPosition.add(movement), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)
                );
        this.stepMoveAndHit(blockHitResult, originalPosition.add(movement));

        spawnStepMoveParticle(originalPosition);

        applyPhysics();

        super.tick();

        if (!level().isClientSide()) {
            if (spellItem == null) {
                LOGGER.error("未设置法术物品");
                discard();
            }

            final SpellAttributes spellAttributes = getMainSpellAttributes();
            if (spellAttributes == null) return;

            int remainingLifeTick = entityData.get(REMAINING_LIFE_TICK);
            if (remainingLifeTick > 0) {
                entityData.set(REMAINING_LIFE_TICK, remainingLifeTick - 1);
            } else {
                onWillDiscard();
                discard();
            }

            int suffixTimer = entityData.get(SUFFIX_TIMER);
            if (suffixTimer > 0) {
                entityData.set(SUFFIX_TIMER, suffixTimer - 1);
            } else if (suffixTimer != -1) {
                castSuffixes(SuffixType.Timer);
                entityData.set(SUFFIX_TIMER, -1);
            }
        }
    }

    protected Collection<EntityHitResult> findHitEntities(Vec3 from, Vec3 to) {
        return ProjectileUtil.getManyEntityHitResult(
                this.level(), this, from, to, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity, false
        );
    }

    protected ProjectileDeflection hitTargetsOrDeflectSelf(@NonNull Collection<EntityHitResult> entityHitResults) {
        for (EntityHitResult e : entityHitResults) {
            ProjectileDeflection deflection = this.hitTargetOrDeflectSelf(e);
            if (!this.isAlive() || deflection != ProjectileDeflection.NONE) {
                return deflection;
            }
        }

        return ProjectileDeflection.NONE;
    }

    protected void stepMoveAndHit(BlockHitResult blockHitResult, Vec3 movementEnd) {
        while (this.isAlive()) {
            Vec3 initialPosition = this.position();
            ArrayList<EntityHitResult> entitiesHit = new ArrayList<>(this.findHitEntities(initialPosition, blockHitResult.getLocation()));
            entitiesHit.sort(Comparator.comparingDouble(c -> initialPosition.distanceToSqr(c.getEntity().position())));

            EntityHitResult firstEntityHit = entitiesHit.isEmpty() ? null : entitiesHit.getFirst();
            Vec3 nextLocation = Objects.requireNonNullElse(firstEntityHit, blockHitResult).getLocation();

            this.setPos(nextLocation);
            if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
                this.handlePortal();
            }

            if (entitiesHit.isEmpty()) {
                if (this.isAlive() && blockHitResult.getType() != HitResult.Type.MISS) {
                    if (EventHooks.onProjectileImpact(this, blockHitResult))
                        break;
                    this.hitTargetOrDeflectSelf(blockHitResult);
                    this.needsSync = true;
                }
                break;
            } else if (this.isAlive() && !this.noPhysics && firstEntityHit.getType() != HitResult.Type.MISS) {
                if (EventHooks.onProjectileImpact(this, firstEntityHit))
                    break;
                ProjectileDeflection deflection = this.hitTargetsOrDeflectSelf(entitiesHit);
                this.needsSync = true;
                if ((this.isPenetrating || this.isPiercing) && deflection == ProjectileDeflection.NONE) {
                    continue;
                }
                break;
            }
        }
    }

    @Override
    protected boolean canHitEntity(@NonNull Entity entity) {
        if (!entity.canBeHitByProjectile()) {
            return false;
        }

        Entity owner = this.getOwner();
        if (entity == owner && !canHitOwner()) {
            return false;
        }

        if (!this.isPiercing) {
            List<EntityReference<Entity>> hurtEntities = entityData.get(HURT_ENTITIES);
            for (EntityReference<Entity> e : hurtEntities) {
                if (e.matches(entity)) {
                    return false;
                }
            }
        }

        return true;
    }

    @SuppressWarnings("RedundantIfStatement")
    protected boolean canHitOwner() {
        if (!isFriendlyFire) return false;
        SpellAttributes spellAttributes = getMainSpellAttributes();
        if (spellAttributes != null && spellAttributes.time() != null && entityData.get(LIFE_TICK) <= spellAttributes.time().canHitShooterAfter())
            return false;
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected void onHit(@NonNull HitResult hitResult) {
        super.onHit(hitResult);
        castSuffixes(SuffixType.Trigger);
    }

    @Override
    protected void onHitEntity(@NonNull EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        final SpellAttributes spellAttributes = getMainSpellAttributes();

        if (spellAttributes != null && spellAttributes.damage() != null) {
            hurtEntity(hitResult.getEntity());
        }

        if (spellAttributes != null && spellAttributes.damage() != null && !spellAttributes.damage().penetrating() && !spellAttributes.damage().piercing()) {
            onWillDiscard();
            discard();
        }
    }

    protected void hurtEntity(Entity entity) {
        List<EntityReference<Entity>> hurtEntities = entityData.get(HURT_ENTITIES);
        hurtEntities.add(EntityReference.of(entity));
        entityData.set(HURT_ENTITIES, hurtEntities);

        if (level().getRandom().nextFloat() <= critChance) {
            damageMap.forEach((type, damage) -> {
                float critResistance = 0;
                float critDamage = damage * (1 + Math.min(critChance, 1) * (5 * Math.max(1, critChance) - 1) * (1 - critResistance));
                damageMap.put(type, critDamage);
            });
            if (level() instanceof ServerLevel serverLevel) {
                double x = entity.position().x;
                double y = entity.position().y;
                double z = entity.position().z;
                serverLevel.sendParticles(
                        ParticleTypes.CRIT.getType(),
                        x, y, z,
                        10, 0,0, 0, 1
                );
                serverLevel.playSound(null, x, y, z, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.AMBIENT, 1.0f, 1.0f);
            }
        }

        if (level() instanceof ServerLevel serverLevel) {
        damageMap.forEach((type, damage) -> {
            if (damage == 0) return;
            var damageSourceKey = switch (type) {
                case Projectile -> DamageTypes.SPELL_PROJECTILE;
                default -> null;
            };
            if (damageSourceKey != null && damage > 0) {
                DamageSource damageSource = serverLevel.damageSources().source(
                        damageSourceKey,
                        this,
                        getOwner()
                );
                entity.hurtServer(serverLevel, damageSource, damage);
                entity.invulnerableTime = 0;

            } else if (damageSourceKey != null && entity instanceof LivingEntity livingEntity) {
                livingEntity.heal(-damage + Float.MIN_NORMAL);
                DamageSource damageSource = serverLevel.damageSources().source(
                        damageSourceKey,
                        this,
                        getOwner()
                );
                livingEntity.hurtServer(serverLevel, damageSource, Float.MIN_NORMAL);
                livingEntity.invulnerableTime = 0;
                }
            });

            DamageSource damageSource = serverLevel.damageSources().source(
                    net.minecraft.world.damagesource.DamageTypes.EXPLOSION,
                    this,
                    getOwner()
            );
            entity.hurtServer(serverLevel, damageSource, explosion);
            entity.invulnerableTime = 0;
        }
    }

    @Override
    protected void onHitBlock(@NonNull BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        int remainingBounces = entityData.get(REMAINING_BOUNCES);
        if (remainingBounces > 0) {
            entityData.set(REMAINING_BOUNCES, remainingBounces - 1);
            bounce(hitResult, bounceStrength);
            onBounced(hitResult);
        } else {
            bounce(hitResult, 1);
            onWillDiscard();
            discard();
        }

        castSuffixes(SuffixType.Trigger);
    }

    protected void onBounced(BlockHitResult hitResult) {
    }

    protected void bounce(@NonNull BlockHitResult hitResult, float bounceStrength) {
        Vec3 motion = getDeltaMovement();
        Direction dir = hitResult.getDirection();
        Vec3 normal = Vec3.atLowerCornerOf(dir.getUnitVec3i());
        double dot = motion.dot(normal);

        if (dot < 0) {
            setDeltaMovement(motion.subtract(normal.scale(2 * dot)));
            setDeltaMovement(getDeltaMovement().scale(bounceStrength));
        }
    }

    protected void onWillDiscard() {
        applyExplosion();

        castSuffixes(SuffixType.Trigger, SuffixType.Timer, SuffixType.Death);
    }

    protected void applyExplosion() {
        if (level() instanceof ServerLevel serverLevel) {
            if (explosionRadius >= 1 && explosion > 0) {
                LivingEntity indirectSourceEntity = getOwner() instanceof LivingEntity livingEntity ? livingEntity : null;
                ExplosionManager.add(serverLevel, new SpellExplosion(
                        serverLevel,this, indirectSourceEntity, this.position(), explosionRadius, false, diggingPower, explosion, this::canHitEntity
                ));
            }
            if (explosionRadius > 0) {
                serverLevel.sendParticles(
                        new ExplosionParticleOptions(explosionRadius),
                        position().x, position().y, position().z,
                        1, 0, 0, 0, 0);
            }
        }
    }

    protected void castSuffixes(SuffixType type) {
        if (level() instanceof ServerLevel serverLevel) {
            if (suffixes.isEmpty()) return;
            for (Suffix suffix : suffixes) {
                if (suffix.type() != type) continue;
                suffix.chain().cast(serverLevel, position(), getDeltaMovement(), 1, getOwner());
            }
            suffixes.removeIf(suffix -> suffix.type() == type);
        }
    }

    protected void castSuffixes(SuffixType... types) {
        if (level() instanceof ServerLevel serverLevel) {
            if (suffixes.isEmpty()) return;
            List<SuffixType> ts = Arrays.stream(types).toList();
            for (Suffix suffix : suffixes) {
                if (!ts.contains(suffix.type())) continue;
                suffix.chain().cast(serverLevel, position(), getDeltaMovement(), 1, getOwner());
            }
            suffixes.removeIf(suffix -> ts.contains(suffix.type()));
        }
    }

    protected float calculateDamageDependsOnSpeed(float damage) {
        if (damage == 0) return 0;
        if (initialSpeed > 0) {
            float speed = (float) getDeltaMovement().length();
            return damage * speed / initialSpeed;
        } else {
            return Float.MAX_VALUE;
        }
    }

    public static class InitialState {
        public int bounces;
        public int lifeTick;
        public int suffixTimer;
        public float gravity;
        public float friction;
        public Map<DamageType, Float> damageMap = new HashMap<>();
        public float dieSpeedThreshold;
        public float explosion;
        public float explosionRadius;
        public float diggingPower;
        public float critChance;
        public boolean penetrating;
        public boolean piercing;
        public boolean friendlyFire;

        public void setValid() {
            bounces = Math.max(bounces, 0);
            lifeTick = Math.max(lifeTick, 0);
            suffixTimer = Math.max(suffixTimer, 0);
            dieSpeedThreshold = Math.max(dieSpeedThreshold, 0);
            explosionRadius = Math.max(explosionRadius, 0);
            diggingPower = Math.max(diggingPower, 0);
            critChance = Math.max(critChance, 0);
        }
    }
}
