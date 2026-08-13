package dynastxu.noitacore.common.spell;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.entity.projectile.SpellProjectile;
import dynastxu.noitacore.item.SpellItem;
import lombok.NonNull;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public record UnitSpellChain(
        @NonNull Holder<Item> mainSpell,
        @NonNull List<UnitSpellChain> suffixes,
        @NonNull List<Holder<Item>> modifiers
) {
    private static final Function<List<Identifier>, List<Holder<Item>>> DECODE_ITEMS = ids -> ids.stream()
            .map(BuiltInRegistries.ITEM::get)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .<Holder<Item>>map(h -> h)
            .toList();

    private static final Function<List<Holder<Item>>, List<Identifier>> ENCODE_ITEMS = holders -> holders.stream()
            .map(Holder::getKey)
            .filter(Objects::nonNull)
            .map(ResourceKey::identifier)
            .toList();

    public static final Codec<Holder<Item>> ITEM_HOLDER_CODEC = Identifier.CODEC.xmap(
            id -> BuiltInRegistries.ITEM.get(id).orElseThrow(),
            holder -> Objects.requireNonNull(holder.getKey()).identifier()
    );

    public static final Codec<List<Holder<Item>>> ITEM_LIST_CODEC = Codec.list(Identifier.CODEC)
            .xmap(DECODE_ITEMS, ENCODE_ITEMS);

    public static final StreamCodec<RegistryFriendlyByteBuf, UnitSpellChain> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, UnitSpellChain::mainSpell,
            ByteBufCodecs.<RegistryFriendlyByteBuf, UnitSpellChain>list().apply(UnitSpellChain.STREAM_CODEC), UnitSpellChain::suffixes,
            ByteBufCodecs.<RegistryFriendlyByteBuf, Holder<Item>>list().apply(Item.STREAM_CODEC), UnitSpellChain::modifiers,
            UnitSpellChain::new
    );

    private static final Codec<UnitSpellChain> LAZY_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<UnitSpellChain, T>> decode(DynamicOps<T> ops, T input) {
            return CODEC.decode(ops, input);
        }

        @Override
        public <T> DataResult<T> encode(UnitSpellChain input, DynamicOps<T> ops, T prefix) {
            return CODEC.encode(input, ops, prefix);
        }
    };

    public static final Codec<UnitSpellChain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ITEM_HOLDER_CODEC.fieldOf("mainSpell").forGetter(UnitSpellChain::mainSpell),
            Codec.list(LAZY_CODEC).fieldOf("suffixes").forGetter(UnitSpellChain::suffixes),
            ITEM_LIST_CODEC.fieldOf("modifiers").forGetter(UnitSpellChain::modifiers)
            ).apply(instance, UnitSpellChain::new));

    public int size() {
        return suffixes.size() + modifiers.size() + 1;
    }

    public void cast(ServerLevel level, Vec3 pos, Vec3 direction, float speedModifier, Entity owner) {
        Item item = mainSpell().value();
        if (item instanceof SpellItem) {
            SpellAttributes spellAttributes = mainSpell.getData(DataMaps.SPELL_ATTRIBUTES);
            if (spellAttributes != null) {
                float spread = 0;
                if (spellAttributes.motion() != null) {
                    spread = spellAttributes.motion().spread();
                }
                cast(level, pos, direction, spread, speedModifier, owner, EntitySpawnReason.TRIGGERED);
            }
        }
    }

    public void cast(ServerLevel level, Vec3 pos, Vec3 direction, float spread, float speedModifier, Entity caster, EntitySpawnReason reason) {
        Item item = mainSpell.value();
        if (item instanceof SpellItem spellItem) {
            EntityType<? extends SpellProjectile> projectileType = spellItem.getSpellProjectile();
            if (projectileType != null) {
                SpellProjectile projectile = projectileType.create(level, reason);
                SpellAttributes spellAttributes = mainSpell.getData(DataMaps.SPELL_ATTRIBUTES);
                if (projectile != null && spellAttributes != null) {
                    float initialSpeed = 0;

                    if (spellAttributes.motion() != null) {
                        initialSpeed += spellAttributes.motion().initialSpeed();
                    }
                    if (spellAttributes.motion() != null) {
                        spread += spellAttributes.motion().spread();
                    }

                    initialSpeed *= speedModifier;
                    spread = Math.max(0, spread);

                    projectile.setPos(pos);
                    projectile.setOwner(caster);
                    projectile.set(mainSpell, modifiers, initialSpeed);
                    projectile.setSuffixes(suffixes);

                    level.addFreshEntity(projectile);

                    projectile.shoot(direction.x, direction.y, direction.z, initialSpeed, spread);
                }
            }
        }
    }
}
