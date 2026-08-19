package dynastxu.noitacore.client.renderer.state;

import lombok.Setter;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class FunkyRenderState extends EntityRenderState implements Rotable {
    @Setter
    public float xRot;
    @Setter
    public float yRot;
}
