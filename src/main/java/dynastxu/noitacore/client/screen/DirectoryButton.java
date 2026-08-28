package dynastxu.noitacore.client.screen;

import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class DirectoryButton extends Button {
    private final Identifier icon;
    private final int iconSize;

    public DirectoryButton(int x, int y, int width, int height,
                           Component message, Identifier icon, int iconSize,
                           OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.icon = icon;
        this.iconSize = iconSize;
    }

    @Override
    public void extractContents(@NonNull GuiGraphicsExtractor graphics,
                                int mouseX, int mouseY, float a) {
        boolean hasIcon = this.icon != null;
        int textLeft = this.getX() + iconSize + 6;

        if (hasIcon) {
            int iconX = this.getX() + 2;
            int iconY = this.getY() + (this.getHeight() - iconSize) / 2;
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    this.icon,
                    iconX, iconY,
                    iconSize, iconSize
            );
        }

        int textRight = this.getX() + this.getWidth() - 2;
        int centerX = textLeft;

        ActiveTextCollector output = graphics.textRendererForWidget(
                this, GuiGraphicsExtractor.HoveredTextEffects.NONE
        );
        output.acceptScrolling(
                this.getMessage(), centerX, textLeft, textRight,
                this.getY(), this.getY() + this.getHeight()
        );
    }
}
