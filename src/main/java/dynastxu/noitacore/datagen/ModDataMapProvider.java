package dynastxu.noitacore.datagen;

import dynastxu.noitacore.common.spell.DamageType;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.common.spell.SuffixType;
import dynastxu.noitacore.datamap.DataMaps;
import dynastxu.noitacore.datamap.MaterialStats;
import dynastxu.noitacore.datamap.SpellAttributes;
import dynastxu.noitacore.item.Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ModDataMapProvider extends DataMapProvider {
    ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NonNull Provider provider) {
        gatherDefaultBlock(provider);
        gatherSpell(provider);
    }

    private void gatherDefaultBlock(HolderLookup.@NonNull Provider provider) {
        Builder<MaterialStats, Block> blockBuilder = builder(DataMaps.MATERIAL_STATS);
        provider.lookupOrThrow(Registries.BLOCK)
                .listElements()
                .filter(blockReference -> blockReference.key().identifier().getNamespace().equals("minecraft"))
                .forEach(blockReference -> {
                    Block block = blockReference.value();
                    MaterialStats stats = MaterialStats.create(block);
                    if (stats != null) {
                        blockBuilder.add(blockReference.key(), stats, false);
                    }
                });
    }

    private void gatherSpell(HolderLookup.@NonNull Provider provider) {
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
                                0.6f, DamageType.Projectile, 0, 1f/7, false, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                250, 0
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
                                0.6f, DamageType.Projectile, 0, 2f/7, false, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                13, 0
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
                                0.6f, DamageType.Projectile, 0, 2f/7, false, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                13, 0
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
                                0.6f, DamageType.Projectile, 0.5f, 12f/7, false, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                13, 0
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
                                0.6f, DamageType.Projectile, 0, 2f/7, false, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                13, 0
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
                                15f, DamageType.Projectile, 50, 250f/7, true, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                120, 0
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
                                50f, DamageType.Projectile, 100, 400f/7, true, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                120, 0
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
                                0, DamageType.Projectile, 0, 2f/7, false, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(10, 0))
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
                                0.75f, DamageType.Projectile, 2, 2f/7, false, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                17, 0
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
        spellBuilder.add(
                Items.SPELL_BLACK_HOLE.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                new SpellAttributes.Uses(3, true),
                                180, 27, 0, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                0, DamageType.Projectile, 0, 1f/7, true, false, true, 1
                        ))
                        .time(new SpellAttributes.Time(
                                40, 3
                        ))
                        .motion(new SpellAttributes.Motion(
                                2f/7, 0, 0, -0.2f, 0.3f, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(200).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_BLACK_HOLE_DEATH_TRIGGER.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                new SpellAttributes.Uses(3, true),
                                200, 30, 0, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                0, DamageType.Projectile, 0, 1f/7, true, false, true, 1
                        ))
                        .time(new SpellAttributes.Time(
                                40, 3
                        ))
                        .motion(new SpellAttributes.Motion(
                                2f/7, 0, 0, -0.2f, 0.3f, 0, 0
                        ))
                        .suffix(new SpellAttributes.Suffix(
                                1, SuffixType.Death, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(220).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_BUCKSHOT.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                25, 3, 0, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                0.925f, DamageType.Projectile, 0, 4f/7, false, false, false, 3
                        ))
                        .time(new SpellAttributes.Time(
                                40, 0
                        ))
                        .motion(new SpellAttributes.Motion(
                                55f/14, 2.3f, 0, 1, 0.06f, 0, 1
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(160).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_LASER_EMITTER.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                60, 2, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 20, 0, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                12f, DamageType.Projectile, 0, 0, false, true, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                33, 0
                        ))
                        .motion(new SpellAttributes.Motion(
                                2f/7, 0, 0, 7, 0.05f, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(180).diggingStrength(11).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_TELEPORT_PROJECTILE.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                40, 1, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 0, -2, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                0, DamageType.Projectile, 0, 2f/7, false, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                13, 0
                        ))
                        .motion(new SpellAttributes.Motion(
                                40f/7, 0, 200, 1.7f, 0.04f, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(130).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_TELEPORT_PROJECTILE_SHORT.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                20, 0, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 0, -2, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                0, DamageType.Projectile, 0, 2f/7, false, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                3, 0
                        ))
                        .motion(new SpellAttributes.Motion(
                                135f/14, 0, 200, 1.7f, 0.04f, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(130).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_DISC_BULLET.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                            SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                20, 3, 0, 0
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1, 20, 0, 0
                        ))
                        .damage(new SpellAttributes.Damage(
                                4, DamageType.Slice, 0, 0, true, false, false, 1
                        ))
                        .time(new SpellAttributes.Time(
                                250, 2
                        ))
                        .motion(new SpellAttributes.Motion(
                                20f/7, 0, 250, 0.6f, 0.05f, 5f/7, 2
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(120).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_DISC_BULLET_BIG.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Projectile,
                                SpellAttributes.Uses.UNRESTRICTED,
                                38, 7, 0, 0
                        ))
                        .modifications(SpellAttributes.Modifications.builder()
                                .spread(3.4f).recoil(20).build())
                        .damage(SpellAttributes.Damage.builder()
                                .damage(12.5f).friendlyFire(true).damageType(DamageType.Slice).build())
                        .time(SpellAttributes.Time.builder()
                                .lifeTick(100).build())
                        .motion(SpellAttributes.Motion.builder()
                                .initialSpeed(25f/14).bounces(2).spread(0.6f).mass(0.05f).build())
                        .other(SpellAttributes.Other.builder()
                                .basePrice(180).build()).build(),
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
        spellBuilder.add(
                Items.SPELL_ORBIT_LASERS.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Modifier,
                                SpellAttributes.Uses.UNRESTRICTED,
                                100, 0, 0, 1
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(200).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_SPEED.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Modifier,
                                SpellAttributes.Uses.UNRESTRICTED,
                                3, 0, 0, 1
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                2.5f, 0, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(100).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_ACCELERATING_SHOT.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Modifier,
                                SpellAttributes.Uses.UNRESTRICTED,
                                20, 3, 0, 1
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                0.32f, 10, 0, 0
                        ))
                        .motion(new SpellAttributes.Motion(
                                0, 0, 0, -3, 0, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(190).build()).build(),
                replace
        );
        spellBuilder.add(
                Items.SPELL_DECELERATING_SHOT.getKey(),
                SpellAttributes.builder()
                        .base(new SpellAttributes.BaseAttributes(
                                SpellType.Modifier,
                                SpellAttributes.Uses.UNRESTRICTED,
                                10, -3, 0, 1
                        ))
                        .modifications(new SpellAttributes.Modifications(
                                1.68f, -10, 0, 0
                        ))
                        .motion(new SpellAttributes.Motion(
                                0, 0, 0, 6, 0, 0, 0
                        ))
                        .other(SpellAttributes.Other.builder()
                                .basePrice(80).build()).build(),
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
