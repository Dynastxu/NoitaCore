package dynastxu.noitacore.item;

import dynastxu.noitacore.common.spell.TickManager;
import dynastxu.noitacore.entity.projectile.SpellProjectile;
import lombok.Getter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class SpellItem extends Item {
    public SpellItem(@NonNull Properties properties) {
        super(properties.stacksTo(1));
    }

    public static class Projectile extends SpellItem {
        @Getter
        private final EntityType<? extends SpellProjectile> projectileType;

        public Projectile(@NonNull Properties properties, EntityType<? extends SpellProjectile> projectileType) {
            super(properties);
            this.projectileType = projectileType;
        }
    }

    public static class Multicast extends SpellItem {
        public Multicast(@NonNull Properties properties) {
            super(properties);
        }
    }

    public static class Modifier extends SpellItem {
        public Modifier(@NonNull Properties properties) {
            super(properties);
        }

        public @Nullable <T extends SpellProjectile> TickManager<T> getTickManager(T projectile) {
            return null;
        }
    }
}
