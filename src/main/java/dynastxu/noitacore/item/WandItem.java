package dynastxu.noitacore.item;

import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.WandData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class WandItem extends Item {
    public WandItem(@NonNull Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void inventoryTick(@NonNull ItemStack itemStack, @NonNull ServerLevel level, @NonNull Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);
        WandData data = itemStack.get(DataComponents.WAND_DATA);

        if (data != null && data.isCooling()) {
            itemStack.set(DataComponents.WAND_DATA, data.cooldown());
        }
    }
}
