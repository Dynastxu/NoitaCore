package dynastxu.noitacore.common.wand;

import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.item.SpellItem;
import dynastxu.noitacore.utils.Utils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class WandContainer implements Container {
    private final int capacity;
    private final NonNullList<ItemStack> inventory;
    private final ItemStack wand;

    public WandContainer(int capacity, NonNullList<ItemStack> inventory, ItemStack wand) {
        this.capacity = capacity;
        this.inventory = Utils.copy(inventory);
        this.wand = wand;
    }

    @Override
    public int getContainerSize() {
        return capacity;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : inventory) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NonNull ItemStack getItem(int i) {
        return i >= 0 && i < getContainerSize() ? inventory.get(i) : ItemStack.EMPTY;
    }

    /**
     *
     * @see WandContainer#removeItem(int)
     */
    @Override
    public @NonNull ItemStack removeItem(int i, int count) {
        return removeItem(i);
    }

    public @NonNull ItemStack removeItem(int i) {
        if (i >= 0 && i < getContainerSize()) {
            ItemStack result = inventory.get(i);
            inventory.set(i, ItemStack.EMPTY);
            if (!result.isEmpty()) {
                setChanged();
            }
            return result;
        }
        return ItemStack.EMPTY;
    }

    /**
     *
     * @see WandContainer#removeItem(int)
     */
    @Override
    public @NonNull ItemStack removeItemNoUpdate(int i) {
        return removeItem(i);
    }

    @Override
    public void setItem(int i, @NonNull ItemStack itemStack) {
        inventory.set(i, itemStack);
        setChanged();
    }

    @Override
    public void setChanged() {
        if (wand.isEmpty()) return;

        WandData wandData = wand.get(DataComponents.WAND_DATA);
        if (wandData != null) {
            wand.set(DataComponents.WAND_DATA.get(), wandData.toBuilder().inventory(Utils.copy(inventory)).build().reload());
        }
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize(@NonNull ItemStack itemStack) {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, @NonNull ItemStack itemStack) {
        return itemStack.getItem() instanceof SpellItem;
    }

    @Override
    public boolean canTakeItem(@NonNull Container into, int slot, @NonNull ItemStack itemStack) {
        return false;
    }
}
