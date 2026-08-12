package dynastxu.noitacore.datagen;

import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.common.spell.DamageType;
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

        // Projectile
        spellBuilder.add(
                Items.SPELL_RUBBER_BALL.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                5, -1, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 0, -1, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                0.6f, DamageType.Projectile, 0, 1f/7, false, false, false
                        ))
                        .time(new SpellAttributes.Time(
                                250
                        ))
                        .motion(new SpellAttributes.Motion(
                                5, 0.6f, 250, 0.6f, 0.03f, 1f / 28, 10
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(60).build()).build(),
                true
        );
        spellBuilder.add(
                Items.SPELL_LIGHT_BULLET.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                5, 1, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 0, -1, 0.05f
                        ))
                        .damage(new SpellAttributes.Damage(
                                0.6f, DamageType.Projectile, 0, 2f/7, false, false, false
                        ))
                        .time(new SpellAttributes.Time(
                                13
                        ))
                        .motion(new SpellAttributes.Motion(
                                40f/7, 0, 200, 1.7f, 0.04f, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(60).diggingStrength(8).diggingPower(40).build()
                        ).build(),
                true
        );

        // Modifier
        spellBuilder.add(
                Items.SPELL_MANA_REDUCE.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Modifier,
                                SpellAttributes.Uses.UNRESTRICTED,
                                -30, 3, 0, 1
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(250).build()).build(),
                true
        );


        // Multicast
        spellBuilder.add(
                Items.SPELL_BURST_2.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Multicast,
                                SpellAttributes.Uses.UNRESTRICTED,
                                0, 0, 0, 2
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(140).build()).build(),
                true
        );
        spellBuilder.add(
                Items.SPELL_BURST_3.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Multicast,
                                SpellAttributes.Uses.UNRESTRICTED,
                                2, 0, 0, 3
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(160).build()).build(),
                true
        );
        spellBuilder.add(
                Items.SPELL_BURST_4.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Multicast,
                                SpellAttributes.Uses.UNRESTRICTED,
                                5, 0, 0, 4
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(180).build()).build(),
                true
        );
        spellBuilder.add(
                Items.SPELL_BURST_8.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Multicast,
                                SpellAttributes.Uses.UNRESTRICTED,
                                30, 0, 0, 8
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(300).build()).build(),
                true
        );
        spellBuilder.add(
                Items.SPELL_BURST_X.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Multicast,
                                new SpellAttributes.Uses(30, false),
                                50, 0, 0, 99
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(500).build()).build(),
                true
        );
    }
}
