package dynastxu.noitacore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dynastxu.noitacore.client.model.NukeModel;
import dynastxu.noitacore.entity.projectile.Nuke;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public class NukeRenderer extends EntityRenderer<Nuke, RotatableEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/entity/nuke.png");
    private final NukeModel model;

    public NukeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new NukeModel(context.bakeLayer(NukeModel.LAYER_LOCATION));
    }

    @Override
    public @NonNull RotatableEntityRenderState createRenderState() {
        return new RotatableEntityRenderState();
    }

    @Override
    public void extractRenderState(@NonNull Nuke entity, @NonNull RotatableEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        RotatableEntityRenderState.extractFromVelocity(entity, state, partialTicks);
    }

    @Override
    public void submit(@NonNull RotatableEntityRenderState state, @NonNull PoseStack poseStack,
                       @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + 180.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));
        poseStack.translate(0.0F, -0.75F, 0.0F);
        collector.submitModel(
                this.model, state, poseStack, TEXTURE,
                state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
