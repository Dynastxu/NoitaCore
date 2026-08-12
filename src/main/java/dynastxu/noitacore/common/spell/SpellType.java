package dynastxu.noitacore.common.spell;

import dynastxu.noitacore.datagen.ITranslatableEnum;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public enum SpellType implements ITranslatableEnum {
    Projectile,
    Static,
    Passive,
    Utility,
    Modifier,
    Material,
    Multicast,
    Other;

    @Override
    public @NonNull String getTranslationKey() {
        return "enum." + MODID + ".spell_type." + this.name();
    }
}
