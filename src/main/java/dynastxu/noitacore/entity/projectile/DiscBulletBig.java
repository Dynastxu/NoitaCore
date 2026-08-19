package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.entity.EntityTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class DiscBulletBig extends AbstractDiscBullet {
    private static final float ACCELERATION_TO_OWNER = 0.25f;

    protected static final EntityDataAccessor<Boolean> BOUNCED =
            SynchedEntityData.defineId(DiscBulletBig.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(BOUNCED, false);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Bounced", entityData.get(BOUNCED));
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(BOUNCED, input.getBooleanOr("Bounced", false));
    }

    public DiscBulletBig(EntityType<? extends AbstractDiscBullet> type, Level level) {
        super(type, level);
    }

    @Override
    protected void spawnCorpse() {
        SpellProjectileCorpse.spawn(this, EntityTypes.DISC_BULLET_BIG_CORPSE.get());
    }

    @Override
    protected void onBounced(BlockHitResult hitResult) {
        super.onBounced(hitResult);
        entityData.set(BOUNCED, true);
    }

    @Override
    public void tick() {
        if (entityData.get(BOUNCED)) {
            Entity owner = getOwner();
            if (owner != null && owner.isAlive()) {
                Vec3 direction = owner.position().subtract(this.position()).normalize();
                setDeltaMovement(getDeltaMovement().add(direction.scale(ACCELERATION_TO_OWNER)));
            }
        }
        super.tick();
    }
}
