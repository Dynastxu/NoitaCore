package dynastxu.noitacore.client.gui;

import com.mojang.logging.LogUtils;
import dynastxu.noitacore.client.font.Font;
import dynastxu.noitacore.common.wand.Caster;
import dynastxu.noitacore.common.wand.WandStatistics;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.WandData;
import dynastxu.noitacore.item.WandItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class WandHudOverlay {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;
    private static final int BACKGROUND_COLOR = 0xFF000000;
    private static final int MANA_COLOR = 0xFF4488FF;
    private static final int MANA_HIGHLIGHT = 0xFF88BBFF;
    private static final int MANA_DRAIN_COLOR = 0x80FFFFFF;
    private static final int MANA_CHARGE_COLOR = 0x804488FF;
    private static final int COOLING_COLOR = 0x80884444;

    @SubscribeEvent
    public static void onRenderGuiLayerPre(RenderGuiLayerEvent.@NonNull Pre event) {
        if (!VanillaGuiLayers.EXPERIENCE_LEVEL.equals(event.getName())) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            return;
        }

        ItemStack stack = getWandStack(player);
        if (stack == null) {
            return;
        }

        WandData data = stack.get(DataComponents.WAND_DATA);
        if (data == null) {
            return;
        }

        event.setCanceled(true);
        renderWandBar(event.getGuiGraphics(), mc, data, player);
    }

    private static @Nullable ItemStack getWandStack(@NonNull Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof WandItem) {
            return mainHand;
        }
        return null;
    }

    private static void renderWandBar(@NonNull GuiGraphicsExtractor guiGraphics, @NonNull Minecraft mc, @NonNull WandData data, Player player) {
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int x = screenWidth / 2 - BAR_WIDTH / 2;
        int y = screenHeight - 29;

        WandStatistics stats = data.statistics();
        int mana = data.mana();
        int manaMax = stats.manaMax();

        // 背景
        guiGraphics.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, BACKGROUND_COLOR);

        if (manaMax > 0) {
            // 法力值
            int manaWidth = (int) ((float) mana / manaMax * BAR_WIDTH);
            if (manaWidth > 0) {
                guiGraphics.fill(x, y, x + manaWidth, y + BAR_HEIGHT, MANA_COLOR);
                guiGraphics.fill(x, y, x + manaWidth, y + 1, MANA_HIGHLIGHT);
            }

            if (!data.statistics().shuffle()) {
                // 法力消耗预测
                int manaDrainWidth = (int) ((float) data.nextCastManaDrain(new Caster<>(player)) / manaMax * BAR_WIDTH);
                if (manaDrainWidth > 0) {
                    guiGraphics.fill(x + manaWidth - manaDrainWidth, y, x + manaWidth, y + BAR_HEIGHT, MANA_DRAIN_COLOR);
                } else if (manaDrainWidth < 0) {
                    guiGraphics.fill(x + manaWidth, y, x + manaWidth - manaDrainWidth, y + BAR_HEIGHT, MANA_CHARGE_COLOR);
                }
            }
        }

        if (data.isCooling()) {
            int coolingWidth;
            if (data.castDelayTick() > data.rechargeTick()) {
                coolingWidth = (int) ((float) data.castDelayTick() / data.lastCastDelayTick() * BAR_WIDTH);
            } else {
                coolingWidth = (int) ((float) data.rechargeTick() / data.lastRechargeTick() * BAR_WIDTH);
            }

            if (coolingWidth > 0) {
                guiGraphics.fill(x, y, x + coolingWidth, y + BAR_HEIGHT, COOLING_COLOR);
            }
        }

        if (data.isCooling()) {
            float coolingTime = (float) Math.max(data.castDelayTick(), data.rechargeTick()) / 20;
            String coolingText = String.format(coolingTime < 1 ? "%.2fs" : "%.0fs", coolingTime);
            guiGraphics.centeredText(mc.font, Component.literal((data.castDelayTick() >= data.rechargeTick() ? Font.CAST_DELAY : Font.RECHARGE) + " " + coolingText),
                    x + BAR_WIDTH / 2, y - BAR_HEIGHT / 2, 0xFFFFFFFF);
        } else {
            if (player.isShiftKeyDown()) {
                String manaText = mana + "/" + manaMax;
                guiGraphics.centeredText(mc.font, Component.literal(manaText),
                        x + BAR_WIDTH / 2, y - BAR_HEIGHT / 2, 0xFFFFFFFF);
            } else {
                float progress = (float) mana / manaMax * 100;
                String manaText = String.format("%.0f%%", progress);
                guiGraphics.centeredText(mc.font, Component.literal(manaText),
                        x + BAR_WIDTH / 2, y - BAR_HEIGHT / 2, 0xFFFFFFFF);
            }
        }
    }
}
