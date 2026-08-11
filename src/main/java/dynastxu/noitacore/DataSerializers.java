package dynastxu.noitacore;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

import static dynastxu.noitacore.NoitaCore.MODID;

public final class DataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MODID);

    public static final Supplier<EntityDataSerializer<List<EntityReference<Entity>>>> HURT_ENTITIES_SERIALIZER =
            ENTITY_DATA_SERIALIZERS.register("hurt_entities", () -> EntityDataSerializer.forValueType(
                    ByteBufCodecs.<ByteBuf, EntityReference<Entity>>list().apply(EntityReference.streamCodec())));
}
