package dynastxu.noitacore.components;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class DataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENT_TYPES =
            DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MODID);

    public static final Supplier<DataComponentType<SpellData>> SPELL_DATA =
            COMPONENT_TYPES.register("spell_data", () -> DataComponentType.<SpellData>builder()
                    .persistent(SpellData.CODEC)
                    .networkSynchronized(SpellData.STREAM_CODEC)
                    .build()
            );

    public static final Supplier<DataComponentType<WandData>> WAND_DATA =
            COMPONENT_TYPES.register("wand_data", () -> DataComponentType.<WandData>builder()
                    .persistent(WandData.CODEC)
                    .networkSynchronized(WandData.STREAM_CODEC)
                    .build()
            );
}
