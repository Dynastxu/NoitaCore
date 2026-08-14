package dynastxu.noitacore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dynastxu.noitacore.client.model.FunkyModel;
import dynastxu.noitacore.entity.projectile.Funky;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import org.jspecify.annotations.NonNull;

import static dynastxu.noitacore.NoitaCore.MODID;

public class FunkyRenderer extends EntityRenderer<Funky, RotatableEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/entity/funky.png");
    private final FunkyModel model;

    public FunkyRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FunkyModel(context.bakeLayer(FunkyModel.LAYER_LOCATION));
    }

    @Override
    public @NonNull RotatableEntityRenderState createRenderState() {
        return new RotatableEntityRenderState();
    }

    @Override
    public void extractRenderState(@NonNull Funky entity, @NonNull RotatableEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        RotatableEntityRenderState.extractFromVelocity(entity, state, partialTicks);
    }

    @Override
    public void submit(@NonNull RotatableEntityRenderState state, @NonNull PoseStack stack, @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        stack.pushPose();
        stack.translate(0.0F, 1F/14, 0.0F);
        stack.scale(0.1f, 0.1f, 0.1f);
        stack.mulPose(Axis.YP.rotationDegrees(state.yRot + 180.0F));
        stack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));
        collector.submitModel(
                this.model, state, stack, TEXTURE,
                LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        stack.popPose();
        super.submit(state, stack, collector, camera);
    }
}
