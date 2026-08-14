package dynastxu.noitacore.world.level.explosion;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Earthquake extends ServerExplosion {
    protected static final BlockInteraction BLOCK_INTERACTION = BlockInteraction.TRIGGER_BLOCK;
    protected static final int MAX_FALL = 64;

    protected final ServerLevel level;
    protected final Entity source;
    protected final LivingEntity indirectSource;
    protected final Vec3 center;
    protected final float radius;
    protected int tick;
    protected List<BlockPos> toFall = new ArrayList<>();

    public Earthquake(ServerLevel level, Entity source, LivingEntity indirectSource, Vec3 center, float radius) {
        super(level, source, null, null, center, radius, false, BLOCK_INTERACTION);
        this.level = level;
        this.source = source;
        this.indirectSource = indirectSource;
        this.center = center;
        this.radius = radius;
    }

    public boolean tick() {
        tick++;

        int fall = 0;
        for (BlockPos pos : toFall) {
            BlockPos below = pos.below();
            if (level.getBlockState(below).isCollisionShapeFullBlock(level, below)) {
                continue;
            }

            FallingBlockEntity.fall(level, pos, level.getBlockState(pos));
            if (++fall >= MAX_FALL) {
                break;
            }
        }
        for (int i = 0; i < fall; i++) {
            toFall.removeFirst();
        }

        if (tick <= radius) {
            level.gameEvent(source, GameEvent.EXPLODE, center);

            int intRadius = tick;
            BlockPos centerPos = BlockPos.containing(center);
            double radiusSq = radius * radius;

            for (int x = -intRadius; x <= intRadius; x++) {
                for (int y = -intRadius; y <= intRadius; y++) {
                    for (int z = -intRadius; z <= intRadius; z++) {
                        if (Math.abs(x) < intRadius && Math.abs(y) < intRadius && Math.abs(z) < intRadius) {
                            continue;
                        }

                        BlockPos pos = centerPos.offset(x, y, z);

                        if (Vec3.atCenterOf(pos).distanceToSqr(center) > radiusSq) {
                            continue;
                        }

                        BlockState state = level.getBlockState(pos);

                        if (state.isAir()) {
                            continue;
                        }

                        if (isIndestructible(state, pos)) {
                            continue;
                        }

                        toFall.add(pos);
                    }
                }
            }
        }

        return tick > radius && toFall.isEmpty();
    }

    @Override
    public int explode() {
        ExplosionManager.add(level, this);
        return 0;
    }

    private boolean isIndestructible(@NonNull BlockState state, BlockPos pos) {
        return state.getDestroySpeed(level, pos) < 0;
    }

    @Override
    public @NonNull ServerLevel level() {
        return level;
    }

    @Override
    public @NonNull BlockInteraction getBlockInteraction() {
        return BLOCK_INTERACTION;
    }

    @Override
    public @Nullable LivingEntity getIndirectSourceEntity() {
        return indirectSource;
    }

    @Override
    public @Nullable Entity getDirectSourceEntity() {
        return source;
    }

    @Override
    public float radius() {
        return radius;
    }

    @Override
    public @NonNull Vec3 center() {
        return center;
    }

    @Override
    public boolean canTriggerBlocks() {
        return true;
    }

    @Override
    public boolean shouldAffectBlocklikeEntities() {
        return BLOCK_INTERACTION.shouldAffectBlocklikeEntities();
    }
}
