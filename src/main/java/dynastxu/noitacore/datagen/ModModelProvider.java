package dynastxu.noitacore.datagen;

import dynastxu.noitacore.block.Blocks;
import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.item.Items;
import dynastxu.noitacore.item.SpellItem;
import dynastxu.noitacore.item.WandItem;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
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

        r.registerTrivialCube(Blocks.BRICKWORK, "brickwork");
    }

    private record Register(BlockModelGenerators blockModel, ItemModelGenerators itemModel) {
        private void registerSpell(Item item, @NonNull SpellType spellType) {
            String layerPath = switch (spellType) {
                case Projectile -> "spell_layer_projectile";
                case Static -> "spell_layer_static";
                case Passive -> "spell_layer_passive";
                case Utility -> "spell_layer_utility";
                case Modifier -> "spell_layer_modifier";
                case Material -> "spell_layer_material";
                case Multicast -> "spell_layer_multicast";
                case Other -> "spell_layer_other";
            };

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
            this.blockModel.createTrivialCube(block.get());
            this.itemModel.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(id));
        }
    }
}
