package dynastxu.noitacore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dynastxu.noitacore.client.model.LaserModel;
import dynastxu.noitacore.client.renderer.state.LaserEmitterRenderState;
import dynastxu.noitacore.entity.projectile.LaserEmitter;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

import java.util.Random;
import java.util.stream.IntStream;

import static dynastxu.noitacore.NoitaCore.MODID;

public class LaserEmitterRenderer extends EntityRenderer<LaserEmitter, LaserEmitterRenderState> {
    private static final RenderType BILLBOARD_RENDER_TYPE = RenderTypes.entityCutout(
            Identifier.fromNamespaceAndPath(MODID, "textures/entity/laser_emitter.png"));
    private static final Identifier[] TEXTURES = IntStream.range(0, 16)
            .mapToObj(i -> Identifier.fromNamespaceAndPath(MODID, "textures/entity/laser_emitter_" + i + ".png"))
            .toArray(Identifier[]::new);
    private final LaserModel model;
    private final Random random = new Random();

    public LaserEmitterRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new LaserModel(context.bakeLayer(LaserModel.LAYER_LOCATION));
    }

    @Override
    public @NonNull LaserEmitterRenderState createRenderState() {
        return new LaserEmitterRenderState();
    }

    @Override
    public void extractRenderState(@NonNull LaserEmitter entity, @NonNull LaserEmitterRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (state.ageInTicks <= 2.0F) {
            state.yRot = -entity.getYRot() + 180.0F;
            state.xRot = -entity.getXRot();
        } else {
            state.yRot = -Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot()) + 180.0F;
            state.xRot = -Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot());
        }
        state.length = 16;
        state.thickness = 1f;
    }

    @Override
    public void submit(@NonNull LaserEmitterRenderState state, @NonNull PoseStack poseStack,
                       @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
        poseStack.scale(state.thickness, state.thickness, state.length/8   );
        poseStack.translate(0, 0.125, 0);
        collector.submitModel(
                this.model, state, poseStack, TEXTURES[random.nextInt(16)],
                LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(camera.orientation);
        collector.submitCustomGeometry(poseStack, BILLBOARD_RENDER_TYPE,
                LaserEmitterRenderer::buildQuad);
        poseStack.popPose();

        super.submit(state, poseStack, collector, camera);
    }

    private static void buildQuad(PoseStack.Pose pose, VertexConsumer buffer) {
        vertex(buffer, pose, 0.0F, 0, 0, 1);
        vertex(buffer, pose, 1.0F, 0, 1, 1);
        vertex(buffer, pose, 1.0F, 1, 1, 0);
        vertex(buffer, pose, 0.0F, 1, 0, 0);
    }

    private static void vertex(@NonNull VertexConsumer builder, PoseStack.Pose pose,
                               float x, int y, int u, int v) {
        builder.addVertex(pose, x - 0.5F, y - 0.25F, 0.0F)
                .setColor(-1)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightCoordsUtil.FULL_BRIGHT)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
