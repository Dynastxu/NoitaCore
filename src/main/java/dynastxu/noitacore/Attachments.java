package dynastxu.noitacore;

import dynastxu.noitacore.common.explosion.ExplosionManager;
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
}
