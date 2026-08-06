package dynastxu.noitacore;

import dynastxu.noitacore.common.spell.SpellAttributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public final class DataMaps {
    public static final DataMapType<Item, SpellAttributes> SPELL_ATTRIBUTES =
            DataMapType.builder(
                    Identifier.parse(MODID + ":spell_attributes"),
                    Registries.ITEM,
                    SpellAttributes.CODEC
            ).build();

    @SubscribeEvent
    public static void registerDataMaps(@NonNull RegisterDataMapTypesEvent event) {
        event.register(SPELL_ATTRIBUTES);
    }
}
