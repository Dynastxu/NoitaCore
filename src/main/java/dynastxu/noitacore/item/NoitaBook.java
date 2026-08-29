package dynastxu.noitacore.item;

import dynastxu.noitacore.attachment.Attachments;
import dynastxu.noitacore.attachment.UnlockedSpells;
import dynastxu.noitacore.network.NoitaBookOpenPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

public class NoitaBook extends Item {
    public NoitaBook(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        openGui(player);
        return InteractionResult.SUCCESS;
    }

    private void openGui(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            UnlockedSpells unlockedSpells = serverPlayer.getData(Attachments.UNLOCKED_SPELLS);
            PacketDistributor.sendToPlayer(serverPlayer, new NoitaBookOpenPayload(unlockedSpells));
        }
    }
}
