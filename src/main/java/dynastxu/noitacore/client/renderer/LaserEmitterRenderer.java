package dynastxu.noitacore.client.renderer;

import dynastxu.noitacore.entity.projectile.LaserEmitter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jspecify.annotations.NonNull;

public class LaserEmitterRenderer extends TextureEntityRenderer<LaserEmitter, EntityRenderState> {
    public LaserEmitterRenderer(EntityRendererProvider.Context context) {
        super(context, "textures/entity/laser_emitter.png");
        scale = 0.5f;
    }

    @Override
    public @NonNull EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
