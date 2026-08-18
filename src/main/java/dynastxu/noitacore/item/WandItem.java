package dynastxu.noitacore.item;

import com.mojang.logging.LogUtils;
import dynastxu.noitacore.common.spell.UnitSpellChain;
import dynastxu.noitacore.common.wand.CastHelper;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.WandData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class WandItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();

    public WandItem(@NonNull Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void inventoryTick(@NonNull ItemStack itemStack, @NonNull ServerLevel level, @NonNull Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);
        WandData data = itemStack.get(DataComponents.WAND_DATA);


        if (data != null) {
            if (data.isCooling()) {
                data = data.cooldown();
                itemStack.set(DataComponents.WAND_DATA, data);
            }
            if (data.mana() < data.statistics().manaMax()) {
                data = data.chargeMana();
                itemStack.set(DataComponents.WAND_DATA, data);
            }
        } else {
            LOGGER.error("法杖物品：未查找到法杖数据");
        }
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        WandData data = itemStack.get(DataComponents.WAND_DATA);
        if (data != null) {
            if (!data.isCooling()) {
                player.startUsingItem(hand);
                return InteractionResult.SUCCESS;
            } else {
                LOGGER.debug("法杖冷却中， 延迟：{}， 充能: {}", data.castDelayTick(), data.rechargeTick());
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public int getUseDuration(@NonNull ItemStack stack, @NonNull LivingEntity entity) {
        return 20;
    }

    @Override
    public void onUseTick(@NonNull Level level, @NonNull LivingEntity livingEntity, @NonNull ItemStack itemStack, int ticksRemaining) {
        super.onUseTick(level, livingEntity, itemStack, ticksRemaining);

        WandData data = itemStack.get(DataComponents.WAND_DATA);
        if (data != null) {
            if (level instanceof ServerLevel serverLevel) {
                if (!data.isCooling()) {
                    CastHelper castHelper = new CastHelper(data);
                    List<UnitSpellChain> nextCast = castHelper.getNextCast(livingEntity);
                    data = castHelper.getWandDataAfterCast();
                    itemStack.set(DataComponents.WAND_DATA, data);

                    Vec3 pos = getCastPosition(livingEntity);
                    Vec3 direction = livingEntity.getLookAngle();

                    int recoil = castHelper.getRecoil();
                    if (recoil > 0) {
                        livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(direction.reverse().scale(recoil / 100.0)));
                        livingEntity.hurtMarked = true;
                    }

                    for (UnitSpellChain chain: nextCast) {
                        float spread = castHelper.getSpread();
                        float critChance = castHelper.getCritChance();
                        float speedModifier = castHelper.getSpeedModifier();
                        chain.cast(serverLevel, pos, direction, spread, critChance, speedModifier, livingEntity, EntitySpawnReason.SPAWN_ITEM_USE);
                    }
                }
            }
        }
    }

    private static @NonNull Vec3 getCastPosition(@NonNull LivingEntity entity) {
        return entity.getEyePosition().add(entity.getLookAngle().scale(1));
    }
}
