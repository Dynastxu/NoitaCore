package dynastxu.noitacore.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.NonNull;

public class PixelParticle extends SingleQuadParticle {
    public PixelParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite,
                         int color, int lifeTime, float size) {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;
        this(level, x, y, z, xa, ya, za, sprite, r, g, b, a, lifeTime, size);
    }
    public PixelParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite,
                         float r, float g, float b, float a, int lifeTime, float size) {
        super(level, x, y, z, xa, ya, za, sprite);
        this.setColor(r, g, b);
        this.setAlpha(a);
        this.setLifetime(lifeTime);
        this.quadSize = size;
    }

    public PixelParticle(@NonNull ClientLevel level, double x, double y, double z, double xa, double ya, double za, int color, int lifeTime, float size, @NonNull SpriteSet sprites) {
        TextureAtlasSprite sprite = sprites.first();
        this(level, x, y, z, xa, ya, za, sprite, color, lifeTime, size);
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }
}
