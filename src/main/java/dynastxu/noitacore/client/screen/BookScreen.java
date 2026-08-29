package dynastxu.noitacore.client.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dynastxu.noitacore.NoitaCore.MODID;

public abstract class BookScreen extends Screen {
    private static final int BACKGROUND_WIDTH = 345;
    private static final int BACKGROUND_HEIGHT = 345;
    protected static final int PAGE_WIDTH = 125;
    protected static final int PAGE_HEIGHT = 170;
    protected static final int DISTANCE_BETWEEN_PAGES = 175;
    private static final int WIDTH = 345;
    private static final int HEIGHT = 345;
    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(MODID, "textures/gui/book_2.png");
    private int leftPos;
    private int topPos;
    private PageButton forwardButton;
    private PageButton backButton;
    protected final Contents contents;
    private int currentPage = 1;

    protected BookScreen(Minecraft minecraft, Font font, Component title, @NonNull Contents contents) {
        super(minecraft, font, title);
        this.contents = contents;
    }

    public void setCurrentPage(int page) {
        if (page < 1) {
            page = 1;
        } else if (page > contents.pages.size()) {
            page = contents.pages.size();
        }
        currentPage = page;
        onPageChanged(currentPage);
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - BACKGROUND_WIDTH) / 2;
        this.topPos = (this.height - BACKGROUND_HEIGHT) / 2;
        int left = leftPos + 20;
        int top = topPos + 85;
        this.forwardButton = addRenderableWidget(
                new PageButton(left + DISTANCE_BETWEEN_PAGES + PAGE_WIDTH - 23, top + PAGE_HEIGHT, true, _ -> nextPage(), true)
        );
        this.backButton = addRenderableWidget(
                new PageButton(left, top + PAGE_HEIGHT, false, _ -> previousPage(), true)
        );
        contents.createButtons(left, top, this);
        onPageChanged(1);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BACKGROUND,
                this.leftPos, this.topPos,
                0, 0,
                WIDTH, HEIGHT,
                BACKGROUND_WIDTH, BACKGROUND_HEIGHT
        );
    }

    public void nextPage() {
        currentPage = Math.min(currentPage + 2, contents.pages.size());
        onPageChanged(currentPage);
    }

    public void previousPage() {
        currentPage = Math.max(currentPage - 2, 1);
        onPageChanged(currentPage);
    }

    protected void onPageChanged(int page) {
        if (currentPage % 2 == 0) {
            currentPage--;
        }
        updateButtonVisibility();
        contents.onPageChanged(page);
    }

    private void updateButtonVisibility() {
        if (forwardButton != null) {
            forwardButton.visible = currentPage + 2 <= contents.pages.size();
        }
        if (backButton != null) {
            backButton.visible = currentPage > 1;
        }
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        contents.layout(10, 10);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        int left = leftPos + 20;
        int top = topPos + 85;
        Page leftPage = contents.getPage(currentPage);
        Page rightPage = contents.getPage(currentPage + 1);
        leftPage.extractRenderState(graphics, left, top, mouseX, mouseY, partialTicks);
        rightPage.extractRenderState(graphics, left + DISTANCE_BETWEEN_PAGES, top, mouseX, mouseY, partialTicks);

        if (leftPage.showPageNumber) {
            graphics.text(font, String.valueOf(currentPage), left + PAGE_WIDTH / 2, top + PAGE_HEIGHT, 0xFF000000, false);
        }
        if (rightPage.showPageNumber) {
            graphics.text(font, String.valueOf(currentPage + 1), left + DISTANCE_BETWEEN_PAGES + PAGE_WIDTH / 2, top + PAGE_HEIGHT, 0xFF000000, false);
        }
    }

    protected static abstract class Page {
        public int page;
        public boolean showPageNumber = true;

        protected void createButtons(int leftPos, int topPos, BookScreen screen) {}
        protected void layout(int x, int y) {}
        protected void onPageChanged(int page) {}

        protected abstract void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int leftPos, int topPos, int mouseX, int mouseY, float partialTicks);

        public static @NonNull Page empty(int page) {
            Page emptyPage = new Page() {
                @Override
                public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int leftPos, int topPos, int mouseX, int mouseY, float partialTicks) {
                }
            };
            emptyPage.page = page;
            return emptyPage;
        }
    }

    protected static class Content {
        protected final Component title;
        protected final Identifier icon;
        protected final List<Page> pages;

        protected Content(Component title, Identifier icon, Page... pages) {
            this(title, icon, List.of(pages));
        }

        protected Content(Component title, Identifier icon, List<Page> pages) {
            this.title = title;
            this.icon = icon;
            this.pages = pages;
        }

        public int getStartPage() {
            return pages.getFirst().page;
        }

        public void onPageChanged(int page) {
            pages.forEach(p -> p.onPageChanged(page));
        }

        public void initPage(int start) {
            for (Page page : pages) {
                page.page = start++;
            }
        }

        public void createButtons(int leftPos, int topPos, BookScreen screen) {
            pages.forEach(page -> page.createButtons(leftPos, topPos, screen));
        }

        public void layout(int x, int y) {
            pages.forEach(page -> {
                if (page.page % 2 == 0) {
                    page.layout(x + 100, y);
                } else {
                    page.layout(x, y);
                }
            });
        }
    }

    protected static class ContentsTable extends Content {
        protected static final int MAX_LINES = 15;

        protected ContentsTable(List<Content> contents) {
            super(Component.translatable("book." + MODID + ".contents_table"), null, initPages(contents));
        }

        private static @NonNull List<Page> initPages(@NonNull List<Content> contents) {
            List<Page> pages = new ArrayList<>();

            int totalPage = (contents.size() + MAX_LINES - 1) / MAX_LINES;
            for (int i = 0; i < totalPage; i++) {
                int start = i * MAX_LINES;
                int end = Math.min(start + MAX_LINES, contents.size());
                List<Content> slice = new ArrayList<>(contents.subList(start, end));
                pages.add(new ContentsTablePage(slice));
            }

            return pages;
        }

        protected static class ContentsTablePage extends Page {
            protected static final int CONTENT_HEIGHT = 10;
            protected static final int CONTENT_WIDTH = 200;
            protected static final int ICON_SIZE = 10;
            private final List<Content> contents;
            private final List<Button> buttons = new ArrayList<>();

            ContentsTablePage(List<Content> contents) {
                this.contents = contents;
            }

            @Override
            public void onPageChanged(int page) {
                hideOrShowButtons(page);
            }

            @Override
            public void createButtons(int leftPos, int topPos, BookScreen screen) {
                topPos += 10;
                buttons.clear();
                int i = 0;
                for (Content content : contents) {
                    if (content instanceof ContentsTable) {
                        continue;
                    }
                    Button button = new DirectoryButton(
                            leftPos, topPos + CONTENT_HEIGHT * i++,
                            CONTENT_WIDTH, CONTENT_HEIGHT,
                            content.title.copy().withStyle(ChatFormatting.BLUE),
                            content.icon,
                            ICON_SIZE,
                            _ -> screen.setCurrentPage(content.getStartPage())
                    );
                    button.visible = false;
                    screen.addRenderableWidget(button);
                    buttons.add(button);
                }
            }

            @Override
            public void layout(int x, int y) {
                int top = y;
                for (Button button : buttons) {
                    if (!button.visible) {
                        continue;
                    }
                    button.setX(x);
                    button.setY(top);
                    top += CONTENT_HEIGHT + 5;
                }
            }

            void hideOrShowButtons(int page) {
                boolean visible = this.page == page || this.page == page + 1;
                for (Button button : buttons) {
                    button.visible = visible;
                }
            }

            @Override
            public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int leftPos, int topPos, int mouseX, int mouseY, float partialTicks) {
                Minecraft minecraft = Minecraft.getInstance();
                Font font = minecraft.font;

                graphics.text(font, Component.translatable("book." + MODID + ".contents_table"), leftPos, topPos, 0xFF000000, false);
            }
        }
    }

    protected static class Contents {
        protected final List<Content> contents;
        protected final List<Page> pages;

        protected Contents(Content... contents) {
            this(new ArrayList<>(Arrays.asList(contents)));
        }

        protected Contents(@NonNull List<Content> contents) {
            this.contents = new ArrayList<>(contents);
            this.pages = new ArrayList<>();

            ContentsTable contentsTable = new ContentsTable(contents);
            this.contents.addFirst(contentsTable);
            contentsTable.initPage(1);
            this.pages.addAll(contentsTable.pages);        // 先加入目录页
            for (Content content : contents) {
                if (content instanceof ContentsTable) {    // 跳过目录，已经处理过了
                    continue;
                }
                if (this.pages.size() % 2 != 0) {
                    this.pages.add(Page.empty(this.pages.size() + 1));
                }
                content.initPage(this.pages.size() + 1);   // initPage 在 addAll 之前
                this.pages.addAll(content.pages);
            }
        }

        public Page getPage(int page) {
            if (page > pages.size()) {
                return Page.empty(page);
            }
            return pages.get(page - 1);
        }

        public void onPageChanged(int page) {
            contents.forEach(content -> content.onPageChanged(page));
        }

        public void createButtons(int leftPos, int topPos, BookScreen screen) {
            contents.forEach(content -> content.createButtons(leftPos, topPos, screen));
        }

        public void layout(int x, int y) {
            contents.forEach(content -> content.layout(x, y));
        }
    }
}
