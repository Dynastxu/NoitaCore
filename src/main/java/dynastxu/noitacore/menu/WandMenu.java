package dynastxu.noitacore.menu;

import dynastxu.noitacore.MenuTypes;
import dynastxu.noitacore.common.wand.WandContainer;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.item.SpellItem;
import lombok.Getter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class WandMenu extends AbstractContainerMenu {
    private static final int SLOTS_PER_ROW = 11;
    private static final int ROWS_PER_PAGE = 3;
    private static final int SLOTS_PER_PAGE = SLOTS_PER_ROW * ROWS_PER_PAGE;

    private final WandContainer wandContainer;
    private final Inventory playerInventory;
    private final ItemStack wand;
    private final DataSlot currentPage;
    @Getter
    private final int totalPages;

    public WandMenu(int containerId, Inventory playerInventory, int containerSize) {
        this(containerId, playerInventory, NonNullList.withSize(containerSize, ItemStack.EMPTY), ItemStack.EMPTY);
    }

    public WandMenu(int containerId, Inventory playerInventory, @NonNull NonNullList<ItemStack> inventory, ItemStack wand) {
        super(MenuTypes.getWandMenuType(inventory.size()).get(), containerId);
        this.wandContainer = new WandContainer(inventory.size(), inventory, wand);
        this.playerInventory = playerInventory;
        this.wand = wand;
        this.currentPage = DataSlot.standalone();
        this.totalPages = (int) Math.ceil((double) wandContainer.getContainerSize() / SLOTS_PER_PAGE);
        this.addDataSlot(currentPage);

        addWandSlots();
        addStandardInventorySlots(playerInventory, 35, 84);
    }

    public int getCurrentPage() {
        return currentPage.get();
    }

    private void addWandSlots() {
        int left = 17;
        int top = 18;

        for (int i = 0; i < wandContainer.getContainerSize(); i++) {
            int page = i / SLOTS_PER_PAGE;
            int posInPage = i % SLOTS_PER_PAGE;
            int row = posInPage / SLOTS_PER_ROW;
            int col = posInPage % SLOTS_PER_ROW;

            int x = left + col * 18;
            int y = top + row * 18;
            this.addSlot(new Slot(wandContainer, i, x, y) {
                @Override
                public boolean isActive() {
                    return page == currentPage.get();
                }

                @Override
                public void setChanged() {
                    super.setChanged();

                }
            });
        }
    }

    @Override
    public boolean clickMenuButton(@NonNull Player player, int buttonId) {
        if (buttonId == 0 && currentPage.get() > 0) {
            currentPage.set(currentPage.get() - 1);
            return true;
        } else if (buttonId == 1 && currentPage.get() < totalPages - 1) {
            currentPage.set(currentPage.get() + 1);
            return true;
        }
        return false;
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        Slot slot = getSlot(slotIndex);
        ItemStack item = slot.getItem();
        ItemStack result = ItemStack.EMPTY;

        final ItemStack originalItem = item.copy();
        if (slot.container instanceof WandContainer) {
            if (moveItemStackTo(item, 0, wandContainer.getContainerSize(), false)) {
                result = originalItem;
            }
        } else if (slot.container instanceof Inventory) {
            if (moveItemStackTo(item, wandContainer.getContainerSize(), wandContainer.getContainerSize() + playerInventory.getContainerSize(), false)) {
                result = originalItem;
            }
        }

        return result;
    }

    @Override
    protected boolean moveItemStackTo(@NonNull ItemStack itemStack, int startSlot, int endSlot, boolean backwards) {
        if (slots.get(startSlot).container instanceof WandContainer && slots.get(endSlot - 1).container instanceof Inventory) {
            if (!(itemStack.getItem() instanceof SpellItem)) {
                return false;
            }
        }

        return super.moveItemStackTo(itemStack, startSlot, endSlot, backwards);
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return player.getMainHandItem() == wand;
    }

    @Override
    public void removed(@NonNull Player player) {
        super.removed(player);
        ItemStack carried = this.getCarried();
        if (!carried.isEmpty()) {
            player.drop(carried, false);
            this.setCarried(ItemStack.EMPTY);
        }
    }
}
