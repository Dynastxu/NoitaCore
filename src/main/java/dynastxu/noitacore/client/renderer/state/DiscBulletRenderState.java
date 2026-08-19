package dynastxu.noitacore.client.renderer.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class DiscBulletRenderState extends EntityRenderState implements Rotable {
    public final AnimationState spinAnimationState = new AnimationState();
    public float xRot;
    public float yRot;

    @Override
    public float getXRot() {
        return xRot;
    }

    @Override
    public float getYRot() {
        return yRot;
    }

    @Override
    public void setXRot(float xRot) {
        this.xRot = xRot;
    }

    @Override
    public void setYRot(float yRot) {
        this.yRot = yRot;
    }
}
