package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.common.spell.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class DiscBullet extends SpellProjectile{
    public DiscBullet(EntityType<? extends SpellProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void hurtEntity(Entity entity) {
        float damage = damageMap.getOrDefault(DamageType.Slice, 0f);
        damageMap.put(DamageType.Slice, calculateDamageDependsOnSpeed(damage));
        super.hurtEntity(entity);
    }
}
