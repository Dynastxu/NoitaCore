package dynastxu.noitacore.entity.projectile;

import com.mojang.serialization.Codec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class SpellProjectileCorpse extends Entity {
    public static final int DEFAULT_LIFE_TICK = 6000;
    protected static final EntityDataAccessor<Integer> REMAINING_LIFE_TICK =
            SynchedEntityData.defineId(SpellProjectileCorpse.class, EntityDataSerializers.INT);

    public SpellProjectileCorpse(EntityType<? extends SpellProjectileCorpse> type, Level level) {
        super(type, level);
    }

    public static void spawn(@NonNull SpellProjectile spellProjectile, @NonNull SpellProjectileCorpse corpse) {
        corpse.setPos(spellProjectile.position());
        corpse.setRemainingLifeTick(DEFAULT_LIFE_TICK);
        corpse.setRot(spellProjectile.getYRot(), spellProjectile.getXRot());
        spellProjectile.level().addFreshEntity(corpse);
    }

    public static void spawn(@NonNull SpellProjectile spellProjectile, EntityType<SpellProjectileCorpse> entityType) {
        SpellProjectileCorpse corpse = new SpellProjectileCorpse(entityType, spellProjectile.level());
        spawn(spellProjectile, corpse);
    }

    public void setRemainingLifeTick(int tick) {
        entityData.set(REMAINING_LIFE_TICK, tick);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        entityData.define(REMAINING_LIFE_TICK, 0);
    }

    @Override
    public boolean hurtServer(@NonNull ServerLevel level, @NonNull DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        entityData.set(REMAINING_LIFE_TICK, input.read("remainingLifeTick", Codec.INT).orElse(0));
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        output.putInt("remainingLifeTick", entityData.get(REMAINING_LIFE_TICK));
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public void tick() {
        if (!isNoGravity() && !onGround()) {
            setDeltaMovement(getDeltaMovement().add(0, -0.04, 0));
        }

        move(MoverType.SELF, getDeltaMovement());

        if (onGround()) {
            setDeltaMovement(Vec3.ZERO);
        }

        super.tick();

        if (entityData.get(REMAINING_LIFE_TICK) > 0) {
            entityData.set(REMAINING_LIFE_TICK, entityData.get(REMAINING_LIFE_TICK) - 1);
        } else {
            discard();
        }
    }
}
