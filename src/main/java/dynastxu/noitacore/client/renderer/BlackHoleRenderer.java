package dynastxu.noitacore.client.renderer;

import dynastxu.noitacore.entity.projectile.BlackHole;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jspecify.annotations.NonNull;

public class BlackHoleRenderer extends TextureEntityRenderer<BlackHole, EntityRenderState> {
    public BlackHoleRenderer(EntityRendererProvider.Context context) {
        super(context, "textures/entity/black_hole.png");
        scale = 6;
    }

    @Override
    public @NonNull EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
