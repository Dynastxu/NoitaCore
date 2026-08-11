package dynastxu.noitacore.particle.explosion;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import org.jspecify.annotations.NonNull;

public class ExplosionParticle extends SingleQuadParticle {
    protected final SpriteSet sprites;

    public ExplosionParticle(ClientLevel level, double x, double y, double z, @NonNull SpriteSet sprites, float size) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;
        this.quadSize = size;
        this.setLifetime(16);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(sprites);
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }
}
