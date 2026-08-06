package dynastxu.noitacore.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class KeyMappings {
    public static final Lazy<KeyMapping> OPEN_WAND_GUI = Lazy.of(() -> new KeyMapping(
            "key." + MODID + ".open_wand_gui",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_TAB,
            KeyMapping.Category.MISC
    ));
}
