package dynastxu.noitacore.common.explosion;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SpellExplosion implements Explosion {
    private static final Logger LOGGER = LogUtils.getLogger();
    /**
     * 每刻所有射线前进的最大总距离
     */
    protected static final float MAX_RAY_DISTANCE_PER_TICK = 17.0F;
    protected static final int MAX_RAYS = 12097;
    protected static final int MIN_RAYS = 64;
    /**
     * 射线单次推进步长
     */
    protected static final float RAY_STEP = 0.5F;
    protected static final BlockInteraction BLOCK_INTERACTION = BlockInteraction.DESTROY_WITH_DECAY;

    protected final ServerLevel level;
    protected final float radius;
    protected final Vec3 center;

    /**
     * 爆炸是否已完全结束
     */
    protected boolean finished = false;
    /**
     * 所有射线
     */
    protected List<Ray> rays;
    /**
     * 累积的被破坏方块位置
     */
    protected List<BlockPos> toBlow = new ArrayList<>();
    protected List<Entity> toHurt = new ArrayList<>();
    protected float energy;
    protected float damage;
    protected Predicate<Entity> canHurt;
    DamageSource damageSource;
    boolean fire;


    public SpellExplosion(ServerLevel level, @Nullable DamageSource damageSource, Vec3 center, float radius, boolean fire, float energy, float damage, Predicate<Entity> canHurt) {
        this.level = level;
        this.damageSource = damageSource;
        this.center = center;
        this.radius = radius;
        this.fire = fire;
        this.energy = energy;
        this.damage = damage;
        this.canHurt = canHurt;
        initRays();
    }

    private static void addOrAppendStack(@NonNull List<StackCollector> stacks, ItemStack stack, BlockPos pos) {
        for (StackCollector stackCollector : stacks) {
            stackCollector.tryMerge(stack);
            if (stack.isEmpty()) {
                return;
            }
        }

        stacks.add(new StackCollector(pos, stack));
    }

    public boolean tick() {
        rays.removeIf(Ray::advance);

        if (rays.isEmpty()) {
            finished = true;
        }

        interactWithBlocks(toBlow);
        hurtEntities(toHurt);
        toBlow.clear();
        toHurt.clear();

        return finished;
    }

    protected void hurtEntities(@NonNull List<Entity> entities) {
        for (Entity entity : entities) {
            entity.hurtServer(level, damageSource, damage);
        }
    }

    protected void interactWithBlocks(@NonNull List<BlockPos> targetBlocks) {
        List<StackCollector> stacks = new ArrayList<>();

        for (BlockPos pos : targetBlocks) {
            this.level.getBlockState(pos).onExplosionHit(this.level, pos, this, (stack, position) -> addOrAppendStack(stacks, stack, position));
        }

        for (StackCollector stack : stacks) {
            Block.popResource(this.level, stack.pos, stack.stack);
        }
    }

    /**
     * 初始化所有射线
     */
    protected void initRays() {
        // 计算球面面积，目标间距 d = 1.0，则每个点约占面积 d^2 = 1
        // 为弥补均匀分布的方差，额外乘以系数 1.5
        double area = 4.0 * Math.PI * this.radius * this.radius;
        int count = (int) Math.ceil(area * 1.5);
        count = Math.max(MIN_RAYS, count);
        count = Math.min(MAX_RAYS, count);

        this.rays = new ArrayList<>(count);
        double goldenRatio = (1.0 + Math.sqrt(5.0)) / 2.0;
        double angleIncrement = Math.PI * 2.0 * goldenRatio;

        for (int i = 0; i < count; i++) {
            double theta = angleIncrement * i;
            double t = (double) i / (count - 1);
            double phi = Math.acos(1.0 - 2.0 * t);
            double x = Math.sin(phi) * Math.cos(theta);
            double y = Math.cos(phi);
            double z = Math.sin(phi) * Math.sin(theta);
            Vec3 direction = new Vec3(x, y, z).normalize();
            this.rays.add(new Ray(direction, energy, this));
        }
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
        return null;
    }

    @Override
    public @Nullable Entity getDirectSourceEntity() {
        return null;
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
        return BLOCK_INTERACTION == BlockInteraction.TRIGGER_BLOCK;
    }

    @Override
    public boolean shouldAffectBlocklikeEntities() {
        boolean mobGriefingEnabled = this.level.getGameRules().get(GameRules.MOB_GRIEFING);
        return mobGriefingEnabled || BLOCK_INTERACTION.shouldAffectBlocklikeEntities();
    }

    protected static class StackCollector {
        private final BlockPos pos;
        private ItemStack stack;

        public StackCollector(BlockPos pos, ItemStack stack) {
            this.pos = pos;
            this.stack = stack;
        }

        public void tryMerge(ItemStack input) {
            if (ItemEntity.areMergable(this.stack, input)) {
                this.stack = ItemEntity.merge(this.stack, input, 16);
            }
        }
    }

    /**
     * 爆炸射线
     */
    public class Ray {
        protected final SpellExplosion explosion;
        /**
         * 单位方向向量
         */
        protected final Vec3 direction;
        /**
         * 当前剩余能量
         */
        protected float energy;
        /**
         * 射线尖端当前坐标
         */
        protected double x, y, z;
        /**
         * 上一个处理过的方块位置，用于避免重复判定
         */
        @Nullable
        protected BlockPos lastPos;
        /**
         * 射线是否已结束
         */
        protected boolean dead;

        /**
         * @param direction 单位方向向量
         * @param energy    初始能量
         */
        public Ray(Vec3 direction, float energy, SpellExplosion explosion) {
            this.explosion = explosion;
            this.direction = direction;
            this.energy = energy;
            this.x = center.x;
            this.y = center.y;
            this.z = center.z;
            this.dead = false;
        }

        public boolean advance() {
            int stepCount = Math.max(1, (int) (MAX_RAY_DISTANCE_PER_TICK / RAY_STEP));
            Vec3 from = new Vec3(x, y, z);

            for (int i = 0; i < stepCount && !dead; i++) {
                step();
            }

            Vec3 to = new Vec3(x, y, z);
            AABB rayAABB = new AABB(from, to).inflate(0.5);

            for (Entity entity : level.getEntities((Entity) null, rayAABB, e -> !toHurt.contains(e) && canHurt.test(e))) {
                entity.getBoundingBox().clip(from, to).ifPresent(_ -> {
                    LOGGER.debug("添加计划伤害的实体： {}", entity.getName());
                    toHurt.add(entity);
                });
            }

            return dead;
        }

        protected void step() {
            double dx = direction.x * RAY_STEP;
            double dy = direction.y * RAY_STEP;
            double dz = direction.z * RAY_STEP;
            x += dx;
            y += dy;
            z += dz;
            BlockPos pos = BlockPos.containing(x, y, z);

            // 离开世界边界
            if (!level.isInWorldBounds(pos)) {
                dead = true;
                return;
            }

            if (new Vec3(x, y, z).distanceTo(center) > radius) {
                dead = true;
                return;
            }

            // 同一方块内不再重复处理
            //noinspection DataFlowIssue
            if (pos.equals(lastPos)) {
                return;
            }
            lastPos = pos;

            BlockState state = level.getBlockState(pos);
            float explosionResistance = state.getExplosionResistance(level, pos, explosion);

            if (energy < explosionResistance) {
                dead = true;
                return;
            }

            // 摧毁方块：记录位置，扣除能量
            toBlow.add(pos);
            energy -= explosionResistance;

            // 能量归零则终止
            if (energy <= 0.0) {
                dead = true;
            }
        }
    }
}
