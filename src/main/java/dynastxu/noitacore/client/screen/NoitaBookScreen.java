package dynastxu.noitacore.client.screen;

import dynastxu.noitacore.attachment.UnlockedSpells;
import dynastxu.noitacore.client.gui.SpellClientTooltipComponent;
import dynastxu.noitacore.client.gui.SpellTooltipComponent;
import dynastxu.noitacore.client.language.EnglishTranslationCache;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.datamap.DataMaps;
import dynastxu.noitacore.datamap.SpellAttributes;
import dynastxu.noitacore.item.Items;
import dynastxu.noitacore.item.SpellItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static dynastxu.noitacore.NoitaCore.ALT_FONT;
import static dynastxu.noitacore.NoitaCore.MODID;

public class NoitaBookScreen extends BookScreen {
    private static final Identifier UNKNOWN_SPELL_TEXTURE = Identifier.fromNamespaceAndPath(MODID, "unknown_spell");

    public NoitaBookScreen(UnlockedSpells unlockedSpells) {
        super(Minecraft.getInstance(), Minecraft.getInstance().font, Component.empty(), contents(unlockedSpells));
    }

    private static @NonNull Contents contents(UnlockedSpells unlockedSpells) {
        Content overview = new Content(
                translatable("overview"),
                null,
                new Page() {
                    @Override
                    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int leftPos, int topPos, int mouseX, int mouseY, float partialTicks) {
                        Minecraft minecraft = Minecraft.getInstance();
                        Font font = minecraft.font;

                        int lineHeight = 16;
                        graphics.text(font, translatable("overview"), leftPos, topPos, 0xFF000000, false);
                        graphics.text(font, translatable("overview.spells_unlocked").append(String.format(" %.1f%% (%d/%d)", unlockedSpells.unlockProgress() * 100, unlockedSpells.spells().size(), Items.SPELL_ITEMS.size())), leftPos, topPos + lineHeight, 0xFF000000, false);
                    }
                }
        );

        Content unlockedSpellsContent = new Content(
                translatable("spells"),
                null,
                unlockedSpellsPages(unlockedSpells)
        );

        return new Contents(overview, unlockedSpellsContent);
    }

    private static @NonNull MutableComponent translatable(String key) {
        return Component.translatable("book." + MODID + "." + key);
    }

    private static @NonNull List<Page> unlockedSpellsPages(@NonNull UnlockedSpells unlockedSpells) {
        List<DeferredItem<? extends SpellItem>> allSpells = Items.SPELL_ITEMS;
        if (allSpells.isEmpty()) {
            return List.of();
        }

        final int ICON_SIZE = 16;
        final int GAP = 2;
        final int CELL_SIZE = ICON_SIZE + GAP;

        final int COLS = (PAGE_WIDTH + GAP) / CELL_SIZE;
        final int ROWS = (PAGE_HEIGHT + GAP) / CELL_SIZE;
        final int PER_PAGE = COLS * ROWS;

        int totalPages = (allSpells.size() + PER_PAGE - 1) / PER_PAGE;

        List<Page> pages = new ArrayList<>();
        for (int p = 0; p < totalPages; p++) {
            int start = p * PER_PAGE;
            int end = Math.min(start + PER_PAGE, allSpells.size());
            List<DeferredItem<? extends SpellItem>> pageSpells = new ArrayList<>(allSpells.subList(start, end));
            pages.add(new SpellsPage(pageSpells, unlockedSpells, COLS));
        }

        return pages;
    }

    private static class SpellsPage extends Page {
        private static final int ICON_SIZE = 16;
        private static final int GAP = 2;
        private static final int CELL_SIZE = ICON_SIZE + GAP;

        private final List<DeferredItem<? extends SpellItem>> spells;
        private final UnlockedSpells unlockedSpells;
        private final int cols;

        SpellsPage(List<DeferredItem<? extends SpellItem>> spells, UnlockedSpells unlockedSpells, int cols) {
            this.spells = spells;
            this.unlockedSpells = unlockedSpells;
            this.cols = cols;
        }

        @Override
        public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int leftPos, int topPos, int mouseX, int mouseY, float partialTicks) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            ItemStack selected = null;
            boolean selectedUnlocked = false;

            for (int i = 0; i < spells.size(); i++) {
                int row = i / cols;
                int col = i % cols;
                int x = leftPos + col * CELL_SIZE;
                int y = topPos + row * CELL_SIZE;

                DeferredItem<? extends SpellItem> deferredItem = spells.get(i);
                Holder<Item> holder = deferredItem.getDelegate();
                SpellType spellType = ((SpellItem) deferredItem.asItem()).spellType;
                String layerPath = spellType.layerPath;

                if (unlockedSpells.spells().contains(holder)) {
                    graphics.fakeItem(new ItemStack(holder.value()), x, y);
                } else {
                    float alpha = 0.5f;
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(MODID, layerPath), x, y, ICON_SIZE, ICON_SIZE, alpha);
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, UNKNOWN_SPELL_TEXTURE, x, y, ICON_SIZE, ICON_SIZE, alpha);
                }

                if (mouseX >= x && mouseX < x + ICON_SIZE && mouseY >= y && mouseY < y + ICON_SIZE) {
                    selected = deferredItem.asItem().getDefaultInstance();
                    selectedUnlocked = unlockedSpells.isUnlocked(holder);
                }
            }

            if (selected != null && player != null) {
                SpellAttributes attributes = selected.getData(DataMaps.SPELL_ATTRIBUTES);
                if (selectedUnlocked) {
                    SpellTooltipComponent component = new SpellTooltipComponent(attributes, null, player);
                    SpellClientTooltipComponent attr = new SpellClientTooltipComponent(component);
                    ClientTooltipComponent name = ClientTooltipComponent.create(Component.translatable(selected.getItem().getDescriptionId()).getVisualOrderText());
                    ClientTooltipComponent desc = ClientTooltipComponent.create(Component.translatable(selected.getItem().getDescriptionId() + ".description").withStyle(ChatFormatting.BLUE).getVisualOrderText());

                    graphics.tooltip(minecraft.font, List.of(name, desc, attr), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, selected.get(DataComponents.TOOLTIP_STYLE));
                }
                else {
                    SpellTooltipComponent component = new SpellTooltipComponent(attributes, null, player, false);
                    SpellClientTooltipComponent attr = new SpellClientTooltipComponent(component);

                    ClientTooltipComponent unknown = ClientTooltipComponent.create(Component.translatable("tooltip.noitacore.unknown").withStyle(ChatFormatting.RED).getVisualOrderText());

                    String nameKey = selected.getItem().getDescriptionId();
                    ClientTooltipComponent name = ClientTooltipComponent.create(
                            EnglishTranslationCache.getComponent(nameKey)
                                    .withStyle(style -> style.withFont(ALT_FONT))
                                    .getVisualOrderText()
                    );

                    String descKey = nameKey + ".description";
                    ClientTooltipComponent desc = ClientTooltipComponent.create(
                            EnglishTranslationCache.getComponent(descKey)
                                    .withStyle(ChatFormatting.BLUE)
                                    .withStyle(style -> style.withFont(ALT_FONT))
                                    .getVisualOrderText()
                    );
                    graphics.tooltip(minecraft.font, List.of(unknown, name, desc, attr), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, selected.get(DataComponents.TOOLTIP_STYLE));
                }
            }
        }
    }
}
