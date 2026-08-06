package dynastxu.noitacore.datagen;

import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.item.Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ModDataMapProvider extends DataMapProvider {
    ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NonNull Provider provider) {
        Builder<SpellAttributes, Item> spellBuilder = builder(DataMaps.SPELL_ATTRIBUTES);

        spellBuilder.add(
                Items.SPELL_RUBBER_BALL.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                5, -1, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 0, -1
                        ))
                        .damage(new SpellAttributes.Damage(
                                0.6f, 0, 1, false, false, false
                        ))
                        .time(new SpellAttributes.Time(
                                250
                        ))
                        .motion(new SpellAttributes.Motion(
                                5, 0.6f, 250, 0.6f, 0.03f, 1f / 28, 10
                        )).build(),
                false
        );
    }
}
