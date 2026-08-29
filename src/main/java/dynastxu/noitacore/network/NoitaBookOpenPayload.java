package dynastxu.noitacore.network;

import dynastxu.noitacore.attachment.UnlockedSpells;
import dynastxu.noitacore.client.screen.NoitaBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public record NoitaBookOpenPayload(
        UnlockedSpells unlockedSpells
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<NoitaBookOpenPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "noita_book_open"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NoitaBookOpenPayload> STREAM_CODEC = StreamCodec.composite(
            UnlockedSpells.STREAM_CODEC, NoitaBookOpenPayload::unlockedSpells,
            NoitaBookOpenPayload::new
    );

    public static void handle(final @NonNull NoitaBookOpenPayload payload, final @NonNull IPayloadContext ignoredContext) {
        Minecraft.getInstance().setScreenAndShow(new NoitaBookScreen(payload.unlockedSpells()));
    }

    @Override
    public @NonNull Type<NoitaBookOpenPayload> type() {
        return TYPE;
    }
}
