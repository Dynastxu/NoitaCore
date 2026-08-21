package dynastxu.noitacore.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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
}
