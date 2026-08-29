package dynastxu.noitacore.common.spell;

import dynastxu.noitacore.datagen.ITranslatableEnum;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public enum SpellType implements ITranslatableEnum {
    Projectile("spell_layer_projectile"),
    Static("spell_layer_static"),
    Passive("spell_layer_passive"),
    Utility("spell_layer_utility"),
    Modifier("spell_layer_modifier"),
    Material("spell_layer_material"),
    Multicast("spell_layer_multicast"),
    Other("spell_layer_other");

    public final String layerPath;

    SpellType(String layerPath) {
        this.layerPath = layerPath;
    }

    @Override
    public @NonNull String getTranslationKey() {
        return "enum." + MODID + ".spell_type." + this.name();
    }
}
