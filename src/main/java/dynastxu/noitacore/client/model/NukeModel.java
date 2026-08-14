package dynastxu.noitacore.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public class NukeModel extends EntityModel<EntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(MODID, "nuke"), "main");

    public NukeModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutCull);
    }

    @SuppressWarnings({"DuplicatedCode", "unused"})
    public static @NonNull LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 6.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, 2.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.0F, 2.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition fin = partdefinition.addOrReplaceChild("fin", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -1.0F, 3.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -1.0F, 2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition fin2 = partdefinition.addOrReplaceChild("fin2", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -1.0F, 3.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -1.0F, 2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition fin3 = partdefinition.addOrReplaceChild("fin3", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -1.0F, 3.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -1.0F, 2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition fin4 = partdefinition.addOrReplaceChild("fin4", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -1.0F, 3.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -1.0F, 2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NonNull EntityRenderState state) {
        root().getAllParts().forEach(ModelPart::resetPose);
    }
}
