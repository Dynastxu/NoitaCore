package dynastxu.noitacore.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.common.spell.Spell;
import dynastxu.noitacore.common.wand.CastHelper;
import dynastxu.noitacore.common.wand.Caster;
import dynastxu.noitacore.common.wand.WandStatistics;
import dynastxu.noitacore.item.SpellItem;
import lombok.Builder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
public record WandData(
        WandStatistics statistics,
        int mana,
        NonNullList<ItemStack> inventory,
        List<Spell> drawStack,
        List<Spell> discardStack,
        int castDelayTick,
        int rechargeTick,
        int lastCastDelayTick,
        int lastRechargeTick,
        boolean needRecharge
) {
    public static final Codec<WandData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    WandStatistics.CODEC.fieldOf("wand_statistics").forGetter(WandData::statistics),
                    Codec.INT.fieldOf("mana").forGetter(WandData::mana),
                    NonNullList.codecOf(ItemStack.OPTIONAL_CODEC).fieldOf("inventory").forGetter(WandData::inventory),
                    Codec.list(Spell.CODEC).fieldOf("draw_stack").forGetter(WandData::drawStack),
                    Codec.list(Spell.CODEC).fieldOf("discard_stack").forGetter(WandData::discardStack),
                    Codec.INT.fieldOf("cast_delay_tick").forGetter(WandData::castDelayTick),
                    Codec.INT.fieldOf("recharge_tick").forGetter(WandData::rechargeTick),
                    Codec.INT.fieldOf("last_cast_delay_tick").forGetter(WandData::lastCastDelayTick),
                    Codec.INT.fieldOf("last_recharge_tick").forGetter(WandData::lastRechargeTick),
                    Codec.BOOL.fieldOf("need_recharge").forGetter(WandData::needRecharge)
            ).apply(instance, WandData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WandData> STREAM_CODEC = StreamCodec.composite(
            WandStatistics.STREAM_CODEC, WandData::statistics,
            ByteBufCodecs.INT, WandData::mana,
            ByteBufCodecs.<RegistryFriendlyByteBuf, Spell>list().apply(Spell.STREAM_CODEC), WandData::drawStack,
            ByteBufCodecs.<RegistryFriendlyByteBuf, Spell>list().apply(Spell.STREAM_CODEC), WandData::discardStack,
            ByteBufCodecs.VAR_INT, WandData::castDelayTick,
            ByteBufCodecs.VAR_INT, WandData::rechargeTick,
            ByteBufCodecs.<RegistryFriendlyByteBuf, ItemStack>list().apply(ItemStack.OPTIONAL_STREAM_CODEC), WandData::inventory,
            ByteBufCodecs.VAR_INT, WandData::lastCastDelayTick,
            ByteBufCodecs.VAR_INT, WandData::lastRechargeTick,
            ByteBufCodecs.BOOL, WandData::needRecharge,
            WandData::new
    );

    public WandData(@NonNull WandStatistics statistics) {
        this(statistics, statistics.manaMax(), NonNullList.withSize(statistics.capacity(), ItemStack.EMPTY), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, 0, false);
    }

    public WandData(@NonNull WandStatistics statistics, int mana, List<Spell> drawStack, List<Spell> discardStack, int castDelayTick, int rechargeTick, List<ItemStack> inventory, int lastCastDelayTick, int lastCastRechargeTick, boolean needRecharge) {
        NonNullList<ItemStack> list = NonNullList.withSize(statistics.capacity(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); i++) {
            list.set(i, inventory.get(i));
        }

        this(statistics, mana, list, drawStack, discardStack, castDelayTick, rechargeTick, lastCastDelayTick, lastCastRechargeTick, needRecharge);
    }

    public boolean isCastDelaying() {
        return castDelayTick > 0;
    }

    public boolean isRecharging() {
        return rechargeTick > 0 && needRecharge;
    }

    public boolean isCooling() {
        return isCastDelaying() || isRecharging();
    }

    public WandData cooldown() {
        if (!isCooling()) {
            return this;
        }
        int castDelayTick = Math.max(0, this.castDelayTick - 1);
        int rechargeTick;
        boolean needRecharge;
        if (this.needRecharge) {
            rechargeTick = Math.max(0, this.rechargeTick - 1);
        } else {
            rechargeTick = this.rechargeTick;
        }
        needRecharge = rechargeTick <= 0 ? false : this.needRecharge;

        return this.toBuilder()
                .castDelayTick(castDelayTick)
                .rechargeTick(rechargeTick)
                .needRecharge(needRecharge).build();
    }

    public WandData chargeMana() {
        if (mana >= statistics.manaMax()) return this;
        int manaCharged = Math.min(mana + statistics.manaChargeSpeed(), statistics.manaMax());
        return this.toBuilder()
                .mana(manaCharged)
                .build();
    }

    private @NonNull List<Spell> getSpells() {
        return getSpells(statistics.capacity(), inventory);
    }

    public static @NonNull List<Spell> getSpells(int capacity, NonNullList<ItemStack> inventory) {
        List<Spell> result = new ArrayList<>();

        for (int i = 0; i < capacity; i++) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof SpellItem) {
                result.add(new Spell(itemStack.typeHolder(), i, false));
            }
        }

        return result;
    }

    public WandData reload() {
        return this.toBuilder().drawStack(getSpells()).discardStack(new ArrayList<>()).build();
    }

    public int nextCastManaDrain(Caster<?> caster) {
        CastHelper castHelper = new CastHelper(this.toBuilder().castDelayTick(0).rechargeTick(0).build());
        Configurator.setLevel("dynastxu.noitacore.common.wand.CastHelper", Level.ERROR);
        castHelper.getNextCast(caster);
        Configurator.setLevel("dynastxu.noitacore.common.wand.CastHelper", Level.DEBUG);
        return this.mana - castHelper.getWandDataAfterCast().mana;
    }
}

