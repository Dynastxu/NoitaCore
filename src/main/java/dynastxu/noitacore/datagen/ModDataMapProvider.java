package dynastxu.noitacore.datagen;

import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.common.spell.DamageType;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.common.spell.SuffixType;
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
        boolean replace = true;

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
                replace
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
                                .basePrice(100).diggingStrength(8).diggingPower(40).build()
                        ).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_LIGHT_BULLET_TRIGGER.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                10, 1, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 0, 0, 0.05f
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
                        .suffix(new SpellAttributes.Suffix(
                                1, SuffixType.Trigger, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(140).diggingStrength(8).diggingPower(40).build()
                        ).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_LIGHT_BULLET_TRIGGER_2.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                15, 1, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 0, 0, 0.05f
                        ))
                        .damage(new SpellAttributes.Damage(
                                0.6f, DamageType.Projectile, 0.5f, 12f/7, false, false, false
                        ))
                        .time(new SpellAttributes.Time(
                                13
                        ))
                        .motion(new SpellAttributes.Motion(
                                5, 0, 200, 1.7f, 0.04f, 0, 0
                        ))
                        .suffix(new SpellAttributes.Suffix(
                                2, SuffixType.Trigger, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(250).diggingStrength(9).diggingPower(40).build()
                        ).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_LIGHT_BULLET_TIMER.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                10, 1, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 0, 0, 0.05f
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
                        .suffix(new SpellAttributes.Suffix(
                                1, SuffixType.Timer, 3
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(140).diggingStrength(8).diggingPower(40).build()
                        ).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_NUKE.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                new SpellAttributes.Uses(1, false),
                                200, 7, 200, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                0.75f, 300, 0, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                15f, DamageType.Projectile, 50, 250f/7, true, false, false
                        ))
                        .time(new SpellAttributes.Time(
                                120
                        ))
                        .motion(new SpellAttributes.Motion(
                                15f/7, 0.6f, 120, 0.00001f, 0.2f, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(400).diggingStrength(8).diggingPower(670).build()
                        ).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_NUKE_GIGA.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                new SpellAttributes.Uses(1, true),
                                500, 17, 267, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                0.5f, 300, 0, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                50f, DamageType.Projectile, 100, 400f/7, true, false, false
                        ))
                        .time(new SpellAttributes.Time(
                                120
                        ))
                        .motion(new SpellAttributes.Motion(
                                45f/7, 0.6f, 120, 0.00001f, 0.2f, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(800).diggingStrength(14).diggingPower(970).build()
                        ).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_CRUMBLING_EARTH.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                new SpellAttributes.Uses(3, false),
                                240, 0, 0, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                0, DamageType.Projectile, 0, 2f/7, false, false, false
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(300).build()
                        ).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_FUNKY.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                5, -1, 0, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                0.75f, DamageType.Projectile, 2, 2f/7, false, false, false
                        ))
                        .time(new SpellAttributes.Time(
                                17
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 0, 2, 1
                        ))
                        .motion(new SpellAttributes.Motion(
                                45f/14, 5.7f, 400, 0, 0.05f, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(50).build()).build(),
                replace
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
                replace
        );
        spellBuilder.add(
                Items.SPELL_CRITICAL_HIT.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Modifier,
                                SpellAttributes.Uses.UNRESTRICTED,
                                5, 0, 0, 1
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                0, 0, 0, 0.15f
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(140).build()).build(),
                replace
        );


        // Multicast
        spellBuilder.add(Items.SPELL_BURST_2.getKey(), buildMulticast(0, 2, 140), replace);
        spellBuilder.add(Items.SPELL_BURST_3.getKey(), buildMulticast(2, 3, 160), replace);
        spellBuilder.add(Items.SPELL_BURST_4.getKey(), buildMulticast(5, 4, 180), replace);
        spellBuilder.add(Items.SPELL_BURST_8.getKey(), buildMulticast(30, 8, 300), replace);
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
                replace
        );
    }

    private SpellAttributes buildMulticast(int manaDrain, int draws, int basePrice) {
        return SpellAttributes.builder()
                .base(new SpellAttributes.BaseAttributes(
                        SpellType.Multicast, SpellAttributes.Uses.UNRESTRICTED,
                        manaDrain, 0, 0, draws
                ))
                .other(SpellAttributes.Other.builder()
                        .basePrice(basePrice)
                        .build()
                )
                .build();
    }
}
