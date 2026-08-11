package dynastxu.noitacore;

import dynastxu.noitacore.datagen.ModLanguageProvider;
import dynastxu.noitacore.item.Items;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class CreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NOITA_SPELL_TAB =
            CREATIVE_MODE_TABS.register("noita_spell_tab", () -> CreativeModeTab.builder()
                    .title(ModLanguageProvider.getTranslatable("noita_spell_tab"))
                    .icon(() -> new ItemStack(Items.SPELL_RUBBER_BALL.get()))
                    .displayItems((_, output) -> {
                        output.accept(Items.SPELL_RUBBER_BALL.get());
                        output.accept(Items.SPELL_LIGHT_BULLET.get());
                    })
                    .build()
            );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NOITA_WAND_TAB =
            CREATIVE_MODE_TABS.register("noita_wand_tab", () -> CreativeModeTab.builder()
                    .title(ModLanguageProvider.getTranslatable("noita_wand_tab"))
                    .icon(() -> new ItemStack(Items.WAND_SMC_SC_NS.get()))
                    .displayItems((_, output) -> {
                        output.accept(Items.WAND_SMC_SC_NS.get());
                        output.accept(Items.WAND_LC_SC_S.get());
                    })
                    .build()
            );
}
