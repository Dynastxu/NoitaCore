package dynastxu.noitacore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dynastxu.noitacore.client.model.DiscBulletModel;
import dynastxu.noitacore.client.renderer.state.DiscBulletRenderState;
import dynastxu.noitacore.entity.projectile.DiscBullet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public class DiscBulletRenderer extends EntityRenderer<DiscBullet, DiscBulletRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/entity/disc_bullet.png");
    private final DiscBulletModel model;

    public DiscBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new DiscBulletModel(context.bakeLayer(DiscBulletModel.LAYER_LOCATION));
    }

    @Override
    public @NonNull DiscBulletRenderState createRenderState() {
        return new DiscBulletRenderState();
    }

    @Override
    public void extractRenderState(@NonNull DiscBullet entity, @NonNull DiscBulletRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.spinAnimationState.startIfStopped(entity.tickCount);
        state.extractRotFromDeltaMovement(entity, partialTicks);
    }

    @Override
    public void submit(@NonNull DiscBulletRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        poseStack.scale(0.25f, 0.25f, 0.25f);
        poseStack.translate(0, 0.125f, 0);
        submitNodeCollector.submitModel(
                this.model, state, poseStack, TEXTURE,
                state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null
        );
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
