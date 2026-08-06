package dynastxu.noitacore.datagen;

import dynastxu.noitacore.item.Items;
import dynastxu.noitacore.common.spell.SpellType;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, MODID);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        spellItem(Items.BOUNCING_BURST_SPELL.get(), itemModels, SpellType.Projectile);

        wandItem(Items.WAND_SMC_SC_NS.get(), itemModels);
        wandItem(Items.WAND_LC_SC_S.get(), itemModels);
    }

    private void spellItem(Item item, @NonNull ItemModelGenerators itemModels, @NonNull SpellType spellType) {
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
        Identifier modelId = itemModels.generateLayeredItem(item, layer0, layer1);

        // 输出客户端物品定义（items/<item_id>.json）
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
    }

    private void wandItem(Item item, @NonNull ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }
}
