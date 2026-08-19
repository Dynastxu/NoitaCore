package dynastxu.noitacore.client.model;

import dynastxu.noitacore.client.renderer.state.DiscBulletRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.entity.animation.json.AnimationHolder;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public class DiscBulletBigModel extends EntityModel<DiscBulletRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(MODID, "disc_bullet_big"), "main");

    public static final AnimationHolder SPIN_ANIMATION =
            Model.getAnimation(Identifier.fromNamespaceAndPath(MODID, "disc_bullet_big"));

    private final KeyframeAnimation spinAnimation;

    public DiscBulletBigModel(ModelPart root) {
        super(root);
        spinAnimation = SPIN_ANIMATION.get().bake(root);
    }

    @SuppressWarnings({"DuplicatedCode", "unused"})
    public static @NonNull LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, -5.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 0).addBox(-0.5F, -2.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 4).addBox(-0.5F, -3.0F, -7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 0).addBox(-0.5F, 3.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 2).addBox(-0.5F, 4.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone3 = bone.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, -5.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 0).addBox(-0.5F, -2.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 4).addBox(-0.5F, -3.0F, -7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 0).addBox(-0.5F, 3.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 2).addBox(-0.5F, 4.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition bone4 = bone.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, -5.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 0).addBox(-0.5F, -2.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 4).addBox(-0.5F, -3.0F, -7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 0).addBox(-0.5F, 3.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 2).addBox(-0.5F, 4.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition bone5 = bone.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, -5.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 0).addBox(-0.5F, -2.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 4).addBox(-0.5F, -3.0F, -7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 0).addBox(-0.5F, 3.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 2).addBox(-0.5F, 4.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(@NonNull DiscBulletRenderState state) {
        super.setupAnim(state);
        this.spinAnimation.apply(state.spinAnimationState, state.ageInTicks);
    }
}
