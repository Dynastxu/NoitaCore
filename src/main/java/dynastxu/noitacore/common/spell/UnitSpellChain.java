package dynastxu.noitacore.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.entity.projectile.SpellProjectile;
import dynastxu.noitacore.item.SpellItem;
import lombok.NonNull;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record UnitSpellChain(
        @NonNull Holder<Item> mainSpell,
        @NonNull List<UnitSpellChain> suffixes,
        @NonNull List<Holder<Item>> modifiers
) {
    public static final StreamCodec<RegistryFriendlyByteBuf, UnitSpellChain> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, UnitSpellChain::mainSpell,
            ByteBufCodecs.<RegistryFriendlyByteBuf, UnitSpellChain>list().apply(UnitSpellChain.STREAM_CODEC), UnitSpellChain::suffixes,
            ByteBufCodecs.<RegistryFriendlyByteBuf, Holder<Item>>list().apply(Item.STREAM_CODEC), UnitSpellChain::modifiers,
            UnitSpellChain::new
    );

    public static final Codec<UnitSpellChain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Item.CODEC.fieldOf("mainSpell").forGetter(UnitSpellChain::mainSpell),
            Codec.list(UnitSpellChain.CODEC).fieldOf("suffixes").forGetter(UnitSpellChain::suffixes),
            Codec.list(Item.CODEC).fieldOf("modifiers").forGetter(UnitSpellChain::modifiers)
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

                    projectile.setPos(pos);
                    projectile.setOwner(caster);
                    projectile.set(mainSpell, modifiers);
                    projectile.setSuffixes(suffixes);

                    level.addFreshEntity(projectile);

                    initialSpeed *= speedModifier;

                    projectile.shoot(direction.x, direction.y, direction.z, initialSpeed, spread);
                }
            }
        }
    }
}
