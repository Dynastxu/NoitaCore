package dynastxu.noitacore.attachment;

import dynastxu.noitacore.world.level.explosion.ExplosionManager;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class Attachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final Supplier<AttachmentType<ExplosionManager>> EXPLOSION_MANAGER =
            ATTACHMENT_TYPES.register("explosion_manager", () -> AttachmentType.builder(ExplosionManager::new).build());

    public static final Supplier<AttachmentType<UnlockedSpells>> UNLOCKED_SPELLS =
            ATTACHMENT_TYPES.register("unclocked_spells", () -> AttachmentType.builder(() -> new UnlockedSpells())
                    .serialize(UnlockedSpells.CODEC)
                    .sync(UnlockedSpells.STREAM_CODEC).build());
}
