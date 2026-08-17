package dynastxu.noitacore.common.spell;

import dynastxu.noitacore.entity.projectile.SpellProjectile;

public abstract class TickManager<T extends SpellProjectile> {
    protected T target;

    public TickManager(T target) {
        this.target = target;
    }

    public abstract void onTick();
}
