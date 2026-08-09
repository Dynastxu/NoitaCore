package dynastxu.noitacore.entity.projectile;

import com.mojang.logging.LogUtils;
import dynastxu.noitacore.DamageTypes;
import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.common.spell.SuffixType;
import dynastxu.noitacore.common.spell.UnitSpellChain;
import dynastxu.noitacore.item.SpellItem;
import lombok.Setter;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import org.slf4j.Logger;

import java.util.*;

public abstract class SpellProjectile extends Projectile {
    protected static final EntityDataAccessor<Integer> REMAINING_BOUNCES =
            SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> REMAINING_LIFE_TICK =
            SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> SUFFIX_TIMER =
            SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);
    private static final Logger LOGGER = LogUtils.getLogger();
    protected Holder<Item> spellItem;
    protected List<Holder<Item>> modifiers;
    @Setter
    protected List<UnitSpellChain> suffixes;
    /**
     * 仅对同一实体造成一次伤害
     */
    protected boolean isPenetrating;
    /**
     * 可对同一实体重复伤害
     */
    protected boolean isPiercing;
    protected float gravity;
    protected float friction;

    public SpellProjectile(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public void set(@NonNull Holder<Item> spellItem, @NonNull List<Holder<Item>> modifiers) {
        this.spellItem = spellItem;
        this.modifiers = modifiers;

        InitialState state = new InitialState();

        SpellAttributes spellAttributes = getMainSpellAttributes();
        if (spellAttributes != null) {
            if (spellAttributes.damage() != null) {
                state.penetrating = spellAttributes.damage().penetrating();
                state.piercing = spellAttributes.damage().piercing();
            }
            if (spellAttributes.motion() != null) {
                state.gravity = spellAttributes.motion().gravity();
                state.bounces = spellAttributes.motion().bounces();
                state.friction = spellAttributes.motion().friction();
            }
            if (spellAttributes.time() != null) {
                state.lifeTick = spellAttributes.time().lifeTick();
            }
            if (spellAttributes.suffix() != null) {
                state.suffixTimer = spellAttributes.suffix().timerLifeTick();
            }
        }

        for (Holder<Item> modifier : modifiers) {
            SpellAttributes modifierAttributes = modifier.getData(DataMaps.SPELL_ATTRIBUTES);
            if (modifierAttributes != null) {
                if (modifierAttributes.damage() != null) {
                    state.penetrating = state.penetrating || modifierAttributes.damage().penetrating();
                    state.piercing = state.piercing || modifierAttributes.damage().piercing();
                }
                if (modifierAttributes.motion() != null) {
                    state.gravity += modifierAttributes.motion().gravity();
                    state.bounces += modifierAttributes.motion().bounces();
                    state.friction += modifierAttributes.motion().friction();
                }
                if (modifierAttributes.time() != null) {
                    state.lifeTick += modifierAttributes.time().lifeTick();
                }
                if (modifierAttributes.suffix() != null) {
                    state.suffixTimer += modifierAttributes.suffix().timerLifeTick();
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

    public void apply(@NonNull InitialState state) {
        state.setValid();

        this.isPiercing = state.penetrating;
        this.isPenetrating = state.penetrating;
        this.gravity = state.gravity;
        this.friction = state.friction;

        this.entityData.set(REMAINING_BOUNCES, state.bounces);
        this.entityData.set(REMAINING_LIFE_TICK, state.lifeTick);
        this.entityData.set(SUFFIX_TIMER, state.suffixTimer);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        entityData.define(REMAINING_BOUNCES, 0);
        entityData.define(REMAINING_LIFE_TICK, 0);
        entityData.define(SUFFIX_TIMER, 0);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("RemainingBounces", entityData.get(REMAINING_BOUNCES));
        output.putInt("RemainingLifeTick", entityData.get(REMAINING_LIFE_TICK));
        output.putInt("SuffixTimer", entityData.get(SUFFIX_TIMER));
        output.putFloat("Gravity", gravity);
        output.putFloat("Friction", friction);
        output.putBoolean("IsPenetrating", isPenetrating);
        output.putBoolean("IsPiercing", isPiercing);
        if (spellItem != null) {
            output.store("SpellItem", Item.CODEC, spellItem);
        }
        if (modifiers != null && !modifiers.isEmpty()) {
            ValueOutput.TypedOutputList<Holder<Item>> modifiersValue = output.list("Modifiers", Item.CODEC);
            modifiers.forEach(modifiersValue::add);
        }
        if (suffixes != null && !suffixes.isEmpty()) {
            ValueOutput.TypedOutputList<UnitSpellChain> suffixesValue = output.list("Suffixes", UnitSpellChain.CODEC);
            suffixes.forEach(suffixesValue::add);
        }
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(REMAINING_BOUNCES, input.getInt("RemainingBounces").orElse(0));
        entityData.set(REMAINING_LIFE_TICK, input.getInt("RemainingLifeTick").orElse(0));
        entityData.set(SUFFIX_TIMER, input.getInt("SuffixTimer").orElse(0));
        gravity = input.getFloatOr("Gravity", 0);
        friction = input.getFloatOr("Friction", 0);
        isPenetrating = input.getBooleanOr("IsPenetrating", false);
        isPiercing = input.getBooleanOr("IsPiercing", false);
        spellItem = input.read("SpellItem", Item.CODEC).orElse(null);
        input.listOrEmpty("Modifiers", Item.CODEC).forEach(modifier -> modifiers.add(modifier));
        input.listOrEmpty("Suffixes", UnitSpellChain.CODEC).forEach(suffix -> suffixes.add(suffix));
    }

    protected SpellAttributes getMainSpellAttributes() {
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
            fax = -(friction / 8400) * getDeltaMovement().x * getDeltaMovement().x;
            fay = -(friction / 8400) * getDeltaMovement().y * getDeltaMovement().y;
            faz = -(friction / 8400) * getDeltaMovement().z * getDeltaMovement().z;
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

    @Override
    public void tick() {
        Vec3 movement = this.getDeltaMovement();
        Vec3 originalPosition = this.position();
        BlockHitResult blockHitResult = this.level()
                .clipIncludingBorder(
                        new ClipContext(originalPosition, originalPosition.add(movement), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)
                );
        this.stepMoveAndHit(blockHitResult);

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

            SuffixType suffixType = null;
            if (spellAttributes.suffix() != null) {
                suffixType = spellAttributes.suffix().type();
            }

            if (suffixType == SuffixType.Timer) {
                int suffixTimer = entityData.get(SUFFIX_TIMER);
                if (suffixTimer > 0) {
                    entityData.set(SUFFIX_TIMER, suffixTimer - 1);
                } else if (suffixTimer != -1) {
                    castSuffixes();
                    entityData.set(SUFFIX_TIMER, -1);
                }
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

    protected void stepMoveAndHit(BlockHitResult blockHitResult) {
        while (this.isAlive()) {
            Vec3 initialPosition = this.position();
            ArrayList<EntityHitResult> entitiesHit = new ArrayList<>(this.findHitEntities(initialPosition, blockHitResult.getLocation()));
            entitiesHit.sort(Comparator.comparingDouble(c -> initialPosition.distanceToSqr(c.getEntity().position())));

            EntityHitResult firstEntityHit = entitiesHit.isEmpty() ? null : entitiesHit.getFirst();
            Vec3 nextLocation = Objects.requireNonNullElse(firstEntityHit, blockHitResult).getLocation();

            this.setPos(nextLocation);
            this.applyEffectsFromBlocks(initialPosition, nextLocation);
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
    public boolean isPassengerOfSameVehicle(@NonNull Entity other) {
        return false;
    }

    @Override
    protected boolean canHitEntity(@NonNull Entity entity) {
        if (entity == getOwner()) {
            final SpellAttributes spellAttributes = getMainSpellAttributes();
            if (spellAttributes != null && spellAttributes.damage() != null && !spellAttributes.damage().friendlyFire()) {
                return false;
            }
        }

        return super.canHitEntity(entity);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected void onHitEntity(@NonNull EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        final SpellAttributes spellAttributes = getMainSpellAttributes();

        if (spellAttributes != null && spellAttributes.damage() != null) {
            onHurtEntity(hitResult.getEntity(), spellAttributes.damage().projectile());
        }

        if (spellAttributes != null && spellAttributes.damage() != null && !spellAttributes.damage().penetrating() && !spellAttributes.damage().piercing()) {
            onWillDiscard();
            discard();
        }
    }

    protected void onHurtEntity(Entity entity, float damage) {
        if (level() instanceof ServerLevel serverLevel) {
            DamageSource damageSource = serverLevel.damageSources().source(
                    DamageTypes.SPELL_PROJECTILE,
                    this,
                    getOwner()
            );
            entity.hurtServer(serverLevel, damageSource, damage);
        }
    }

    @Override
    protected void onHit(@NonNull HitResult hitResult) {
        super.onHit(hitResult);
        needsSync = true;
    }

    @Override
    protected void onHitBlock(@NonNull BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        int remainingBounces = entityData.get(REMAINING_BOUNCES);
        if (remainingBounces > 0) {
            entityData.set(REMAINING_BOUNCES, remainingBounces - 1);

            Vec3 motion = getDeltaMovement();
            Direction dir = hitResult.getDirection();
            Vec3 normal = Vec3.atLowerCornerOf(dir.getUnitVec3i());
            double dot = motion.dot(normal);

            if (dot < 0) {
                // 先把位置推到方块外，避免卡住
                Vec3 newPos = position().add(normal.scale(0.1));
                setPos(newPos);

                setDeltaMovement(motion.subtract(normal.scale(2 * dot)));
                setDeltaMovement(getDeltaMovement().scale(0.5));
            }
        } else {
            onWillDiscard();
            discard();
        }
    }

    protected void onWillDiscard() {
        final SpellAttributes spellAttributes = getMainSpellAttributes();
        if (spellAttributes == null) return;
        SuffixType suffixType = null;
        if (spellAttributes.suffix() != null) {
            suffixType = spellAttributes.suffix().type();
        }
        if (suffixType == SuffixType.Trigger) {
            castSuffixes();
        }
    }

    protected void castSuffixes() {
        if (level() instanceof ServerLevel serverLevel) {
            if (suffixes.isEmpty()) return;
            for (UnitSpellChain suffix : suffixes) {
                suffix.cast(serverLevel, position(), getDeltaMovement(), 1, getOwner());
            }
        }
    }

    public static class InitialState {
        public int bounces = 0;
        public int lifeTick = 0;
        public int suffixTimer = 0;
        public float gravity = 0;
        public float friction = 0;
        public boolean penetrating = false;
        public boolean piercing = false;

        public void setValid() {
            bounces = Math.max(bounces, 0);
            lifeTick = Math.max(lifeTick, 0);
            suffixTimer = Math.max(suffixTimer, 0);
        }
    }
}
