package dynastxu.noitacore.datagen;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jspecify.annotations.NonNull;

public interface ITranslatableEnum extends TranslatableEnum {
    String getTranslationKey();

    @Override
    default @NonNull Component getTranslatedName() {
        return Component.translatable(getTranslationKey());
    }
}
