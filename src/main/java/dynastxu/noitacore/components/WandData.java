package dynastxu.noitacore.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dynastxu.noitacore.common.spell.Spell;
import dynastxu.noitacore.common.wand.WandStatistics;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
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
        int rechargeTick
) {
    public static final Codec<WandData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    WandStatistics.CODEC.fieldOf("wand_statistics").forGetter(WandData::statistics),
                    Codec.INT.fieldOf("mana").forGetter(WandData::mana),
                    NonNullList.codecOf(ItemStack.OPTIONAL_CODEC).fieldOf("inventory").forGetter(WandData::inventory),
                    Codec.list(Spell.CODEC).fieldOf("draw_stack").forGetter(WandData::drawStack),
                    Codec.list(Spell.CODEC).fieldOf("discard_stack").forGetter(WandData::discardStack),
                    Codec.INT.fieldOf("cast_delay_tick").forGetter(WandData::castDelayTick),
                    Codec.INT.fieldOf("recharge_tick").forGetter(WandData::rechargeTick)
            ).apply(instance, WandData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WandData> STREAM_CODEC = StreamCodec.composite(
            WandStatistics.STREAM_CODEC, WandData::statistics,
            ByteBufCodecs.INT, WandData::mana,
            ByteBufCodecs.<ByteBuf, Spell>list().apply(Spell.STREAM_CODEC), WandData::drawStack,
            ByteBufCodecs.<ByteBuf, Spell>list().apply(Spell.STREAM_CODEC), WandData::discardStack,
            ByteBufCodecs.VAR_INT, WandData::castDelayTick,
            ByteBufCodecs.VAR_INT, WandData::rechargeTick,
            ByteBufCodecs.<RegistryFriendlyByteBuf, ItemStack>list().apply(ItemStack.OPTIONAL_STREAM_CODEC), WandData::inventory,
            WandData::new
    );

    public WandData(@NonNull WandStatistics statistics) {
        this(statistics, statistics.manaMax(), NonNullList.withSize(statistics.capacity(), ItemStack.EMPTY), new ArrayList<>(), new ArrayList<>(), 0, 0);
    }

    public WandData(@NonNull WandStatistics statistics, Integer mana, List<Spell> drawStack, List<Spell> discardStack, Integer castDelayTick, Integer rechargeTick, List<ItemStack> inventory) {
        NonNullList<ItemStack> list = NonNullList.withSize(statistics.capacity(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); i++) {
            list.set(i, inventory.get(i));
        }

        this(statistics, mana, list, drawStack, discardStack, castDelayTick, rechargeTick);
    }

    public boolean isCastDelaying() {
        return castDelayTick > 0;
    }

    public boolean isRecharging() {
        return rechargeTick > 0;
    }

    public boolean isCooling() {
        return isCastDelaying() || isRecharging();
    }

    public WandData cooldown() {
        if (!isCooling()) {
            return this;
        }
        int castDelayTick = this.castDelayTick;
        if (--castDelayTick < 0) {
            castDelayTick = 0;
        }
        int rechargeTick = this.rechargeTick;
        if (--rechargeTick < 0) {
            rechargeTick = 0;
        }
        return this.toBuilder()
                .castDelayTick(castDelayTick)
                .rechargeTick(rechargeTick).build();
    }
}

