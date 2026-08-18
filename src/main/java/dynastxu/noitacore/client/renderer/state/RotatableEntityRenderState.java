package dynastxu.noitacore.client.renderer.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class RotatableEntityRenderState extends EntityRenderState {
    public float yRot;
    public float xRot;

    public static void extractFromVelocity(@NonNull Entity entity, RotatableEntityRenderState state, float partialTicks) {
        Vec3 velocity = entity.getDeltaMovement();
        double horizontalDist = velocity.horizontalDistance();
        if (horizontalDist > 1.0E-7 || Math.abs(velocity.y) > 1.0E-7) {
            //noinspection SuspiciousNameCombination
            state.yRot = (float) (Mth.atan2(velocity.x, velocity.z) * (180.0F / Math.PI));
            state.xRot = (float) (Mth.atan2(velocity.y, horizontalDist) * (180.0F / Math.PI));
        } else {
            state.yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
            state.xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        }
    }
}
