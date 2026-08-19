package dynastxu.noitacore.entity.projectile;

import dynastxu.noitacore.common.spell.DamageType;
import dynastxu.noitacore.entity.EntityTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class DiscBullet extends AbstractDiscBullet {
    public DiscBullet(EntityType<? extends DiscBullet> type, Level level) {
        super(type, level);
    }

    @Override
    protected void hurtEntity(Entity entity) {
        float damage = damageMap.getOrDefault(DamageType.Slice, 0f);
        damageMap.put(DamageType.Slice, calculateDamageDependsOnSpeed(damage));
        super.hurtEntity(entity);
    }

    @Override
    protected void spawnCorpse() {
        SpellProjectileCorpse.spawn(this, EntityTypes.DISC_BULLET_CORPSE.get());
    }
}
