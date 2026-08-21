package dynastxu.noitacore.datagen;

import dynastxu.noitacore.item.SpellItem;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public sealed abstract class ModLanguageProvider extends LanguageProvider permits ModLanguageProviderEnUS, ModLanguageProviderZhCN {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, MODID, locale);
    }

    @Override
    protected void addTranslations() {
        addCreativeTabs();
        addItems();
        addToolTips();
        addDamageTypes();
        addEnums();
    }

    protected void addCreativeModeTab(String tab, String value) {
        add("itemGroup." + MODID + "." + tab, value);
    }

    protected void addDamageTypeDefault(String msgId, String msg, String msgByPlayer, String msgByItem) {
        add("death.attack." + msgId, msg);
        add("death.attack." + msgId + ".player", msgByPlayer);
        add("death.attack." + msgId + ".item", msgByItem);
    }

    protected void addSpell(@NonNull DeferredItem<? extends SpellItem> item, String name, String description) {
        addSpell(item.get(), name, description);
    }

    protected void addSpell(Item item, String name, String description) {
        add(item, name);
        add(item.getDescriptionId() + ".description", description);
    }

    protected void addTooltip(String key, String value) {
        add("tooltip." + MODID + "." + key, value);
    }

    protected void addEnum(@NonNull ITranslatableEnum enumValue, String value) {
        add(enumValue.getTranslationKey(), value);
    }

    protected abstract void addCreativeTabs();

    protected abstract void addItems();

    protected abstract void addBlocks();

    protected abstract void addDamageTypes();

    protected abstract void addToolTips();

    protected abstract void addEnums();
}
