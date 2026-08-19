package dynastxu.noitacore.client.renderer.state;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public interface Rotable {
    void setXRot(float xRot);
    void setYRot(float yRot);

    default void extractRotFromDeltaMovement(@NonNull Entity entity, float partialTicks) {
        Vec3 velocity = entity.getDeltaMovement();
        double horizontalDist = velocity.horizontalDistance();
        if (horizontalDist > 1.0E-7 || Math.abs(velocity.y) > 1.0E-7) {
            //noinspection SuspiciousNameCombination
            setYRot((float) (Mth.atan2(velocity.x, velocity.z) * (180.0F / Math.PI)));
            setXRot((float) (Mth.atan2(velocity.y, horizontalDist) * (180.0F / Math.PI)));
        } else {
            setYRot(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()));
            setXRot(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()));
        }
    }
}
