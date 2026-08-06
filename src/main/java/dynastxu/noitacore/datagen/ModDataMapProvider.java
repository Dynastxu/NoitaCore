package dynastxu.noitacore.datagen;

import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.item.Items;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.common.spell.SpellType;
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
                Items.BOUNCING_BURST_SPELL.getKey(),
                SpellAttributes.builder()
                        .base(SpellAttributes.BaseAttributes.builder()
                                .type(SpellType.Projectile)
                                .manaDrain(5)
                                .castDelayTick(-1).build())
                        .modifications(SpellAttributes.Modifications.builder()
                                .spread(-1).build())
                        .damage(SpellAttributes.Damage.builder()
                                .projectile(0.6f)
                                .explosionRadius(1).build())
                        .time(SpellAttributes.Time.builder()
                                .lifeTick(250).build())
                        .motion(SpellAttributes.Motion.builder()
                                .initialSpeed(5)
                                .dieSpeedThreshold(0.04f)
                                .bounces(10)
                                .spread(0.57f)
                                .gravity(250)
                                .friction(0.6f)
                                .mass(0.03f).build()).build(),
                false
        );
    }
}
