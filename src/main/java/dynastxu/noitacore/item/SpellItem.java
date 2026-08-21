package dynastxu.noitacore.item;

import dynastxu.noitacore.common.spell.SpellType;
import dynastxu.noitacore.common.spell.TickManager;
import dynastxu.noitacore.entity.projectile.SpellProjectile;
import lombok.Getter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public sealed abstract class SpellItem extends Item {
    public final SpellType spellType;

    public SpellItem(@NonNull Properties properties, SpellType spellType) {
        super(properties.stacksTo(1));
        this.spellType = spellType;
    }

    public static non-sealed class Projectile extends SpellItem {
        @Getter
        private final EntityType<? extends SpellProjectile> projectileType;

        public Projectile(@NonNull Properties properties, EntityType<? extends SpellProjectile> projectileType) {
            super(properties, SpellType.Projectile);
            this.projectileType = projectileType;
        }
    }

    public static non-sealed class Multicast extends SpellItem {
        public Multicast(@NonNull Properties properties) {
            super(properties, SpellType.Multicast);
        }
    }

    public static non-sealed class Modifier extends SpellItem {
        public Modifier(@NonNull Properties properties) {
            super(properties, SpellType.Modifier);
        }

        public @Nullable <T extends SpellProjectile> TickManager<T> getTickManager(T projectile) {
            return null;
        }
    }

    public static non-sealed class Static extends SpellItem {
        public Static(@NonNull Properties properties) {
            super(properties, SpellType.Static);
        }
    }

    public static non-sealed class Passive extends SpellItem {
        public Passive(@NonNull Properties properties) {
            super(properties, SpellType.Passive);
        }
    }

    public static non-sealed class Utility extends SpellItem {
        public Utility(@NonNull Properties properties) {
            super(properties, SpellType.Utility);
        }
    }

    public static non-sealed class Material extends SpellItem {
        public Material(@NonNull Properties properties) {
            super(properties, SpellType.Material);
        }
    }

    public static non-sealed class Other extends SpellItem {
        public Other(@NonNull Properties properties) {
            super(properties, SpellType.Other);
        }
    }
}
