package dynastxu.noitacore;

import dynastxu.noitacore.attachment.Attachments;
import dynastxu.noitacore.block.Blocks;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.entity.EntityTypes;
import dynastxu.noitacore.item.CreativeTabs;
import dynastxu.noitacore.item.Items;
import dynastxu.noitacore.menu.MenuTypes;
import dynastxu.noitacore.particle.ParticleTypes;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(NoitaCore.MODID)
public final class NoitaCore {
    public static final String MODID = "noitacore";
    public static final FontDescription ALT_FONT = new FontDescription.Resource(Identifier.fromNamespaceAndPath(MODID, "alt"));

    public NoitaCore(IEventBus modEventBus, ModContainer modContainer) {
        Items.ITEMS.register(modEventBus);
        Blocks.BLOCKS.register(modEventBus);
        DataComponents.COMPONENT_TYPES.register(modEventBus);
        CreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        MenuTypes.MENU_TYPES.register(modEventBus);
        EntityTypes.ENTITY_TYPES.register(modEventBus);
        DamageTypes.DAMAGE_TYPES.register(modEventBus);
        ParticleTypes.PARTICLE_TYPES.register(modEventBus);
        Attachments.ATTACHMENT_TYPES.register(modEventBus);
        DataSerializers.ENTITY_DATA_SERIALIZERS.register(modEventBus);
    }
}
