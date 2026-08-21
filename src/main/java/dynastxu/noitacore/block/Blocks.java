package dynastxu.noitacore.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class Blocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> BRICKWORK = BLOCKS.registerSimpleBlock(
            "brickwork",
            () -> BlockBehaviour.Properties.of()
                    .strength(50, 1600)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<SlabBlock> BRICKWORK_SLAB = BLOCKS.registerBlock(
            "brickwork_slab",
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .strength(50, 1600)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<StairBlock> BRICKWORK_STAIR = BLOCKS.registerBlock(
            "brickwork_stair",
            properties -> new StairBlock(BRICKWORK.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of()
                    .strength(50, 1600)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
    );
}
