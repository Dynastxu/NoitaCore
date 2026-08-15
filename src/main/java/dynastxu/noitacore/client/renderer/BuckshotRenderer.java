package dynastxu.noitacore.client.renderer;

import dynastxu.noitacore.entity.projectile.Buckshot;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jspecify.annotations.NonNull;

public class BuckshotRenderer extends TextureEntityRenderer<Buckshot, EntityRenderState> {
    public BuckshotRenderer(EntityRendererProvider.Context context) {
        super(context, "textures/entity/buckshot.png");
        scale = 0.1f;
    }

    @Override
    public @NonNull EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
