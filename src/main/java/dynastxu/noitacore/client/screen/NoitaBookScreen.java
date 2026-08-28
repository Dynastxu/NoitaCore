package dynastxu.noitacore.client.screen;

import dynastxu.noitacore.attachment.UnclockedSpells;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public class NoitaBookScreen extends BookScreen {

    public NoitaBookScreen(UnclockedSpells unclockedSpells) {
        super(Minecraft.getInstance(), Minecraft.getInstance().font, Component.empty(), contents(unclockedSpells));
    }

    private static @NonNull Contents contents(UnclockedSpells unclockedSpells) {
        Content overview = new Content(
                Component.translatable("book." + MODID + ".overview"),
                null,
                new Page() {
                    @Override
                    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int leftPos, int topPos, int mouseX, int mouseY, float partialTicks) {
                        Minecraft minecraft = Minecraft.getInstance();
                        Font font = minecraft.font;

                        graphics.text(font, Component.translatable("book." + MODID + ".overview"), leftPos, topPos, 0xFFFFFFFF);
//                        graphics.text(font, Component.translatable("book." + MODID + ".overview.description"), leftPos, topPos, 0xFFFFFFFF);
//                        graphics.text(font, Component.literal(String.valueOf(page)), 10, 30, 0xFFFFFFFF);
                    }
                }
        );

        return new Contents(overview);
    }
}
