package dynastxu.noitacore.client.renderer;

import dynastxu.noitacore.entity.projectile.RubberBall;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jspecify.annotations.NonNull;

public class RubberBallRenderer extends TextureEntityRenderer<RubberBall, EntityRenderState> {
    public RubberBallRenderer(EntityRendererProvider.Context context) {
        super(context, "textures/entity/rubber_ball.png");
    }

    @Override
    public @NonNull EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
