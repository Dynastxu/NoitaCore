package dynastxu.noitacore.common.spell;

import lombok.NonNull;

import java.util.List;

public record UnitSpellChain(
        @NonNull SpellAttributes spellAttributes,
        List<UnitSpellChain> suffixes,
        List<SpellAttributes> modifiers
) {
    public int size() {
        return suffixes.size() + modifiers.size() + 1;
    }
}
