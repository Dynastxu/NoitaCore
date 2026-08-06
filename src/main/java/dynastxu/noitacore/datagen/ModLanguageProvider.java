package dynastxu.noitacore.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public sealed abstract class ModLanguageProvider extends LanguageProvider permits ModLanguageProviderEnUS, ModLanguageProviderZhCN {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, MODID, locale);
    }

    public static @NonNull Component getTranslatable(String path) {
        return Component.translatable("itemGroup." + MODID + "." + path);
    }

    protected void add(@NonNull DeferredHolder<CreativeModeTab, CreativeModeTab> tab, String value) {
        add("itemGroup." + MODID + "." + tab.getId().getPath(), value);
    }

    protected abstract void addCreativeTabs();

    protected abstract void addItems();
}
