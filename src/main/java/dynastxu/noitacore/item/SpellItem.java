package dynastxu.noitacore.item;

import dynastxu.noitacore.entity.projectile.SpellProjectile;
import lombok.Builder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SpellItem extends Item {
    private final EntityType<? extends SpellProjectile> projectileType;

    public SpellItem(@NonNull Properties properties) {
        this(properties, null);
    }

    @Builder
    public SpellItem(@NonNull Properties properties, EntityType<? extends SpellProjectile> projectileType) {
        super(properties.stacksTo(1));
        this.projectileType = projectileType;
    }

    public @Nullable EntityType<? extends SpellProjectile> getSpellProjectile() {
        return projectileType;
    }

    public void onWillApplyInitialState(SpellProjectile.@NonNull InitialState state) {
    }
}
