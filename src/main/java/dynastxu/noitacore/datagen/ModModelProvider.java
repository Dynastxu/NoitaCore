package dynastxu.noitacore.datagen;

import dynastxu.noitacore.block.Blocks;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.item.Items;
import dynastxu.noitacore.item.SpellItem;
import dynastxu.noitacore.item.WandItem;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, MODID);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        Register r = new Register(blockModels, itemModels);

        Items.SPELL_ITEMS.forEach(r::registerSpell);

        r.registerWand(Items.WAND_SMC_SC_NS);
        r.registerWand(Items.WAND_LC_SC_S);
        r.registerWand(Items.WAND_MLC_3C_S);
        r.registerWand(Items.WAND_OF_DESTRUCTION);

        r.registerWithSlabAndStair(Blocks.BRICKWORK, Blocks.BRICKWORK_SLAB, Blocks.BRICKWORK_STAIR, "brickwork");

        itemModels.generateFlatItem(Items.NOITA_BOOK.get(), ModelTemplates.FLAT_ITEM);
    }

    private record Register(BlockModelGenerators blockModel, ItemModelGenerators itemModel) {
        private void registerSpell(Item item, @NonNull SpellType spellType) {
            String layerPath = spellType.layerPath;

            // 构建两个材质：layer0 是法术类型固定纹理，layer1 是物品自身纹理
            Material layer0 = new Material(Identifier.fromNamespaceAndPath(MODID, "item/" + layerPath));
            Material layer1 = TextureMapping.getItemTexture(item);

            // 生成模型 JSON 并获取其标识符
            Identifier modelId = itemModel.generateLayeredItem(item, layer0, layer1);

            // 输出客户端物品定义（items/<item_id>.json）
            itemModel.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
        }

        private void registerWand(Item item, @NonNull ItemModelGenerators itemModels) {
            itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }

        public void registerWand(@NonNull DeferredItem<WandItem> item) {
            registerWand(item.get(), this.itemModel);
        }

        public <I extends SpellItem> void registerSpell(@NonNull DeferredItem<I> item) {
            registerSpell(item.get(), item.get().spellType);
        }

        void registerTrivialCube(@NonNull DeferredBlock<?> block, String name) {
            registerTrivialCube(block, Identifier.fromNamespaceAndPath(MODID, "block/" + name));
        }

        public void registerTrivialCube(@NonNull DeferredBlock<?> block, Identifier id) {
            DataGen.requireNonAir(block.asItem());

            this.blockModel.createTrivialCube(block.get());
            this.itemModel.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(id));
        }

        public void registerSlab(@NonNull DeferredBlock<?> slabBlock, @NonNull DeferredBlock<?> baseBlock, String name) {
            DataGen.requireNonAir(baseBlock.asItem());
            DataGen.requireNonAir(slabBlock.asItem());

            var texture = TextureMapping.getBlockTexture(baseBlock.get());
            var textureMapping = new TextureMapping()
                    .put(TextureSlot.SIDE, texture)
                    .put(TextureSlot.BOTTOM, texture)
                    .put(TextureSlot.TOP, texture);

            var slabBottomId = Identifier.fromNamespaceAndPath(MODID, "block/" + name + "_slab");
            var slabTopId = Identifier.fromNamespaceAndPath(MODID, "block/" + name + "_slab_top");
            var slabFullId = Identifier.fromNamespaceAndPath(MODID, "block/" + name + "_slab_full");

            var slabBottom = ModelTemplates.SLAB_BOTTOM.create(slabBottomId, textureMapping, this.blockModel.modelOutput);
            var slabTop = ModelTemplates.SLAB_TOP.create(slabTopId, textureMapping, this.blockModel.modelOutput);
            var full = ModelTemplates.CUBE_BOTTOM_TOP.create(slabFullId, textureMapping, this.blockModel.modelOutput);

            this.blockModel.blockStateOutput.accept(
                    BlockModelGenerators.createSlab(
                            slabBlock.get(),
                            new MultiVariant(WeightedList.of(new Variant(slabBottom))),
                            new MultiVariant(WeightedList.of(new Variant(slabTop))),
                            new MultiVariant(WeightedList.of(new Variant(full)))
                    )
            );

            this.itemModel.itemModelOutput.accept(
                    slabBlock.asItem(),
                    ItemModelUtils.plainModel(slabBottomId)
            );
        }

        public void registerStair(@NonNull DeferredBlock<?> stairBlock, @NonNull DeferredBlock<?> baseBlock, String name) {
            DataGen.requireNonAir(baseBlock.asItem());
            DataGen.requireNonAir(stairBlock.asItem());

            var texture = TextureMapping.getBlockTexture(baseBlock.get());
            var textureMapping = new TextureMapping()
                    .put(TextureSlot.SIDE, texture)
                    .put(TextureSlot.BOTTOM, texture)
                    .put(TextureSlot.TOP, texture);

            var straightId = Identifier.fromNamespaceAndPath(MODID, "block/" + name + "_stairs");
            var innerId = Identifier.fromNamespaceAndPath(MODID, "block/" + name + "_stairs_inner");
            var outerId = Identifier.fromNamespaceAndPath(MODID, "block/" + name + "_stairs_outer");

            var straight = ModelTemplates.STAIRS_STRAIGHT.create(straightId, textureMapping, this.blockModel.modelOutput);
            var inner = ModelTemplates.STAIRS_INNER.create(innerId, textureMapping, this.blockModel.modelOutput);
            var outer = ModelTemplates.STAIRS_OUTER.create(outerId, textureMapping, this.blockModel.modelOutput);

            this.blockModel.blockStateOutput.accept(
                    BlockModelGenerators.createStairs(
                            stairBlock.get(),
                            new MultiVariant(WeightedList.of(new Variant(inner))),
                            new MultiVariant(WeightedList.of(new Variant(straight))),
                            new MultiVariant(WeightedList.of(new Variant(outer)))
                    )
            );

            this.itemModel.itemModelOutput.accept(
                    stairBlock.asItem(),
                    ItemModelUtils.plainModel(straightId)
            );
        }

        public void registerWithSlabAndStair(@NonNull DeferredBlock<?> baseBlock, DeferredBlock<?> slabBlock, DeferredBlock<?> stairBlock, String name) {
            registerTrivialCube(baseBlock, name);
            registerSlab(slabBlock, baseBlock, name);
            registerStair(stairBlock, baseBlock, name);
        }
    }
}
