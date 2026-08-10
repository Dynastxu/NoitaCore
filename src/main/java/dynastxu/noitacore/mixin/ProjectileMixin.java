package dynastxu.noitacore.mixin;

import dynastxu.noitacore.accessor.ProjectileAccessor;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Projectile.class)
public class ProjectileMixin implements ProjectileAccessor {
    @Shadow private boolean leftOwner;

    @Override
    public boolean noitaCore$isLeftOwner() {
        return leftOwner;
    }
}
