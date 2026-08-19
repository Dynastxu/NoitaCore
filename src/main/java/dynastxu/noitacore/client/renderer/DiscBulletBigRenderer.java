package dynastxu.noitacore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dynastxu.noitacore.client.model.DiscBulletBigModel;
import dynastxu.noitacore.client.renderer.state.DiscBulletRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public class DiscBulletBigRenderer extends EntityRenderer<Entity, DiscBulletRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/entity/disc_bullet_big.png");
    private final DiscBulletBigModel model;
    private final boolean isCorpse;

    public DiscBulletBigRenderer(EntityRendererProvider.Context context) {
        this(context, false);
    }

    public DiscBulletBigRenderer(EntityRendererProvider.Context context, boolean isCorpse) {
        super(context);
        this.model = new DiscBulletBigModel(context.bakeLayer(DiscBulletBigModel.LAYER_LOCATION));
        this.isCorpse = isCorpse;
    }

    @Override
    public @NonNull DiscBulletRenderState createRenderState() {
        return new DiscBulletRenderState();
    }

    @Override
    public void extractRenderState(@NonNull Entity entity, @NonNull DiscBulletRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (this.isCorpse) {
            state.spinAnimationState.stop();
        } else {
            state.spinAnimationState.startIfStopped(entity.tickCount);
        }
        state.extractRotFromDeltaMovement(entity, partialTicks);
    }

    @Override
    public void submit(@NonNull DiscBulletRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        if (!isCorpse) {
            poseStack.translate(0, 0.5f, 0);
        } else {
            poseStack.translate(0, 0.25f, 0);
        }
        submitNodeCollector.submitModel(
                this.model, state, poseStack, TEXTURE,
                state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null
        );
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
