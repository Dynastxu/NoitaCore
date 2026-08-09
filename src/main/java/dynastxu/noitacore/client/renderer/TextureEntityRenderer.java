package dynastxu.noitacore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public abstract class TextureEntityRenderer< T extends Entity, S extends EntityRenderState> extends EntityRenderer<T, S> {
    private final RenderType renderType;

    TextureEntityRenderer(EntityRendererProvider.Context context, String path) {
        this(context, Identifier.fromNamespaceAndPath(MODID, path));
    }

    protected TextureEntityRenderer(EntityRendererProvider.Context context, Identifier texture) {
        super(context);
        this.renderType = RenderTypes.entityCutout(texture);
    }

    @Override
    public void submit(@NonNull S state, @NonNull PoseStack stack, @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        super.submit(state, stack, collector, camera);
        stack.pushPose();
        stack.scale(0.5F, 0.5F, 0.5F);
        stack.mulPose(camera.orientation);
        collector.submitCustomGeometry(stack, renderType, (pose, buffer) -> buildQuad(state, pose, buffer));
        stack.popPose();
        super.submit(state, stack, collector, camera);
    }

    private static void buildQuad(@NonNull EntityRenderState state, PoseStack.Pose pose, VertexConsumer buffer) {
        vertex(buffer, pose, state.lightCoords, 0.0F, 0, 0, 1);
        vertex(buffer, pose, state.lightCoords, 1.0F, 0, 1, 1);
        vertex(buffer, pose, state.lightCoords, 1.0F, 1, 1, 0);
        vertex(buffer, pose, state.lightCoords, 0.0F, 1, 0, 0);
    }

    private static void vertex(@NonNull VertexConsumer builder, PoseStack.Pose pose, int lightCoords, float x, int y, int u, int v) {
        builder.addVertex(pose, x - 0.5F, y - 0.25F, 0.0F)
                .setColor(-1)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightCoords)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
