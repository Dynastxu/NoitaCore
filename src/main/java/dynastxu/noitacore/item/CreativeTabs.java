package dynastxu.noitacore.item;

import dynastxu.noitacore.datagen.ModLanguageProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class CreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NOITA_SPELL_TAB =
            CREATIVE_MODE_TABS.register("noita_spell_tab", () -> CreativeModeTab.builder()
                    .title(ModLanguageProvider.getTranslatable("noita_spell_tab"))
                    .icon(() -> new ItemStack(Items.SPELL_RUBBER_BALL.get()))
                    .displayItems((_, output) -> {
                        // Projectile
                        output.accept(Items.SPELL_RUBBER_BALL.get());
                        output.accept(Items.SPELL_LIGHT_BULLET.get());
                        output.accept(Items.SPELL_LIGHT_BULLET_TRIGGER.get());
                        output.accept(Items.SPELL_LIGHT_BULLET_TRIGGER_2.get());
                        output.accept(Items.SPELL_LIGHT_BULLET_TIMER.get());
                        output.accept(Items.SPELL_NUKE.get());
                        output.accept(Items.SPELL_NUKE_GIGA.get());
                        output.accept(Items.SPELL_CRUMBLING_EARTH.get());
                        output.accept(Items.SPELL_FUNKY.get());
                        output.accept(Items.SPELL_BLACK_HOLE.get());
                        output.accept(Items.SPELL_BLACK_HOLE_DEATH_TRIGGER.get());
                        output.accept(Items.SPELL_BUCKSHOT.get());
                        output.accept(Items.SPELL_LASER_EMITTER.get());
                        output.accept(Items.SPELL_TELEPORT_PROJECTILE.get());
                        output.accept(Items.SPELL_TELEPORT_PROJECTILE_SHORT.get());
                        output.accept(Items.SPELL_DISC_BULLET.get());
                        // Modifier
                        output.accept(Items.SPELL_MANA_REDUCE.get());
                        output.accept(Items.SPELL_CRITICAL_HIT.get());
                        output.accept(Items.SPELL_ORBIT_LASERS.get());
                        output.accept(Items.SPELL_SPEED.get());
                        output.accept(Items.SPELL_ACCELERATING_SHOT.get());
                        output.accept(Items.SPELL_DECELERATING_SHOT.get());
                        // Multicast
                        output.accept(Items.SPELL_BURST_2.get());
                        output.accept(Items.SPELL_BURST_3.get());
                        output.accept(Items.SPELL_BURST_4.get());
                        output.accept(Items.SPELL_BURST_8.get());
                        output.accept(Items.SPELL_BURST_X.get());
                    })
                    .build()
            );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NOITA_WAND_TAB =
            CREATIVE_MODE_TABS.register("noita_wand_tab", () -> CreativeModeTab.builder()
                    .title(ModLanguageProvider.getTranslatable("noita_wand_tab"))
                    .icon(() -> new ItemStack(Items.WAND_SMC_SC_NS.get()))
                    .displayItems((_, output) -> {
                        output.accept(Items.WAND_SMC_SC_NS.get());
                        output.accept(Items.WAND_LC_SC_S.get());
                        output.accept(Items.WAND_MLC_3C_S.get());
                        output.accept(Items.WAND_OF_DESTRUCTION.get());
                    })
                    .build()
            );
}
