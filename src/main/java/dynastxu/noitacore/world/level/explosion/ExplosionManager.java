package dynastxu.noitacore.world.level.explosion;

import dynastxu.noitacore.attachment.Attachments;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public class ExplosionManager {
    protected List<SpellExplosion> spellExplosions = new ArrayList<>();
    protected List<Earthquake> earthquakes = new ArrayList<>();

    public static void add(@NonNull ServerLevel level, @NonNull SpellExplosion explosion) {
        if (EventHooks.onExplosionStart(level, explosion)) return;
        level.getData(Attachments.EXPLOSION_MANAGER).add(explosion);
    }

    public static void add(@NonNull ServerLevel level, @NonNull Earthquake earthquake) {
        if (EventHooks.onExplosionStart(level, earthquake)) return;
        level.getData(Attachments.EXPLOSION_MANAGER).add(earthquake);
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

    public void add(Earthquake earthquake) {
        earthquakes.add(earthquake);
    }

    public void tick() {
        spellExplosions.removeIf(SpellExplosion::tick);
        earthquakes.removeIf(Earthquake::tick);
    }
}
