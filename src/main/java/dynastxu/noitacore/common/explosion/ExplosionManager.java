package dynastxu.noitacore.common.explosion;

import dynastxu.noitacore.Attachments;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public class ExplosionManager {
    protected List<SpellExplosion> spellExplosions = new ArrayList<>();

    public static void add(@NonNull ServerLevel level, @NonNull SpellExplosion explosion) {
        level.getData(Attachments.EXPLOSION_MANAGER).add(explosion);
    }

    @SubscribeEvent
    public static void tick(ServerTickEvent.@NonNull Post event) {
        event.getServer().getAllLevels().forEach(
                level -> level.getData(Attachments.EXPLOSION_MANAGER).tick()
        );
    }

    public void add(SpellExplosion explosion) {
        spellExplosions.add(explosion);
    }

    public void tick() {
        spellExplosions.removeIf(SpellExplosion::tick);
    }
}
