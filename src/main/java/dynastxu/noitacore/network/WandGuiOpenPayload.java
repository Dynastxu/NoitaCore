package dynastxu.noitacore.network;

import com.mojang.logging.LogUtils;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.item.WandItem;
import dynastxu.noitacore.menu.WandMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import static dynastxu.noitacore.NoitaCore.MODID;

public record WandGuiOpenPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WandGuiOpenPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "wand_gui_open"));
    public static final StreamCodec<ByteBuf, WandGuiOpenPayload> STREAM_CODEC = StreamCodec.unit(new WandGuiOpenPayload());
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void handle(final WandGuiOpenPayload ignoredPayload, final @NonNull IPayloadContext context) {
        Player player = context.player();
        ItemStack mainHand = player.getMainHandItem();

        if (mainHand.getItem() instanceof WandItem) {
            WandData wandData = mainHand.get(DataComponents.WAND_DATA);
            if (wandData != null) {
                NonNullList<ItemStack> wandInventory = wandData.inventory();
                player.openMenu(new MenuProvider() {
                    @Override
                    public AbstractContainerMenu createMenu(int id, @NonNull Inventory inventory, @NonNull Player player) {
                        return new WandMenu(id, inventory, wandInventory, mainHand);
                    }

                    @Override
                    public @NonNull Component getDisplayName() {
                        return mainHand.getItemName();
                    }
                });
            }
        }
    }

    @Override
    public @NonNull Type<WandGuiOpenPayload> type() {
        return TYPE;
    }
}
