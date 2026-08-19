package dynastxu.noitacore.client.renderer.state;

import lombok.Setter;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class DiscBulletRenderState extends EntityRenderState implements Rotable {
    public final AnimationState spinAnimationState = new AnimationState();
    @Setter
    public float xRot;
    @Setter
    public float yRot;
}
