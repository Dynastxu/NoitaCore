package dynastxu.noitacore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dynastxu.noitacore.client.model.NukeModel;
import dynastxu.noitacore.client.renderer.state.NukeRenderState;
import dynastxu.noitacore.entity.projectile.Nuke;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public class NukeRenderer extends EntityRenderer<Nuke, NukeRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/entity/nuke.png");
    private final NukeModel model;

    public NukeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new NukeModel(context.bakeLayer(NukeModel.LAYER_LOCATION));
    }

    @Override
    public @NonNull NukeRenderState createRenderState() {
        return new NukeRenderState();
    }

    @Override
    public void extractRenderState(@NonNull Nuke entity, @NonNull NukeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.extractRotFromDeltaMovement(entity, partialTicks);
    }

    @Override
    public void submit(@NonNull NukeRenderState state, @NonNull PoseStack poseStack,
                       @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.25F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + 180.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));
        collector.submitModel(
                this.model, state, poseStack, TEXTURE,
                state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
