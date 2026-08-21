package dynastxu.noitacore.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Builder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Builder
public record MaterialStats(
    int durability,
    int density,
    float hardness,
    boolean conductive
) {
    public static @NonNull MaterialStatsBuilder builder() {
        return new MaterialStatsBuilder()
                .durability(0)
                .density(0)
                .hardness(0)
                .conductive(false);
    }

    public static final Codec<MaterialStats> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("durability").forGetter(MaterialStats::durability),
            Codec.INT.fieldOf("density").forGetter(MaterialStats::density),
            Codec.FLOAT.fieldOf("hardness").forGetter(MaterialStats::hardness),
            Codec.BOOL.fieldOf("conductive").forGetter(MaterialStats::conductive)
    ).apply(instance, MaterialStats::new));

    public static @Nullable MaterialStats create(@NonNull Block block) {
        float destroyTime = block.defaultDestroyTime();
        if (destroyTime < 0) {
            return new MaterialStats(-1, -1, -1, false);
        }
        if (destroyTime == 0) {
            return new MaterialStats(0, 0, 0, false);
        }
        if (isDirt(block)) {
            return new MaterialStats(10, 10, 1.9f, false);
        }
        if (isWooden(block)) {
            return new MaterialStats(10, 11, 0.2f, false);
        }
        if (isBrick(block)) {
            return new MaterialStats(14, 10, 100, false);
        }
        if (isStoneLike(block)) {
            return new MaterialStats(10, 10, 20, false);
        }
        if (isIce(block)) {
            return new MaterialStats(10, 6, 3, false);
        }
        if (isMetal(block)) {
            return new MaterialStats(12, 10, 13, true);
        }
        return null;
    }

    private static boolean isWooden(@NonNull Block block) {
        return block.defaultBlockState().is(BlockTags.LOGS)
                || block.defaultBlockState().is(BlockTags.PLANKS);
    }

    private static boolean isBrick(@NonNull Block block) {
        return block.defaultBlockState().is(BlockTags.STONE_BRICKS)
                || BuiltInRegistries.BLOCK.getKey(block).getPath().contains("brick");
    }

    private static boolean isStoneLike(@NonNull Block block) {
        if (block.defaultBlockState().is(BlockTags.BASE_STONE_OVERWORLD)
                || block.defaultBlockState().is(BlockTags.BASE_STONE_NETHER)) {
            return true;
        }
        String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
        return path.contains("stone") || path.contains("deepslate");
    }

    private static boolean isMetal(@NonNull Block block) {
        return block.defaultBlockState().getSoundType() == SoundType.METAL;
    }

    private static boolean isIce(@NonNull Block block) {
        return block.defaultBlockState().is(BlockTags.ICE);
    }

    private static boolean isDirt(@NonNull Block block) {
        return block.defaultBlockState().is(BlockTags.DIRT);
    }
}
