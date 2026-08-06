package dynastxu.noitacore.item;

import com.mojang.logging.LogUtils;
import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.common.spell.SpellAttributes;
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
                itemStack.set(DataComponents.WAND_DATA, data.cooldown());
            }
            if (data.mana() < data.statistics().manaMax()) {
                itemStack.set(DataComponents.WAND_DATA, data.chargeMana());
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
                return InteractionResult.CONSUME;
            } else {
                LOGGER.debug("法杖冷却中");
                LOGGER.debug("延迟：{}", data.castDelayTick());
                LOGGER.debug("充能: {}", data.rechargeTick());
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public int getUseDuration(@NonNull ItemStack stack, @NonNull LivingEntity entity) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onUseTick(@NonNull Level level, @NonNull LivingEntity livingEntity, @NonNull ItemStack itemStack, int ticksRemaining) {
        super.onUseTick(level, livingEntity, itemStack, ticksRemaining);

        if (level instanceof ServerLevel serverLevel) {
            WandData data = itemStack.get(DataComponents.WAND_DATA);
            if (data != null && !data.isCooling()) {
                List<UnitSpellChain> nextCast = CastHelper.getNextCast(livingEntity, itemStack);
                Vec3 pos = getCastPosition(livingEntity);
                Vec3 direction = livingEntity.getLookAngle();
                float speedModifier = data.statistics().speedMultiplier();
                nextCast.forEach(chain -> {
                    float spread = data.statistics().spread();
                    SpellAttributes spellAttributes = chain.mainSpell().getData(DataMaps.SPELL_ATTRIBUTES);
                    if (spellAttributes != null) {
                        if (spellAttributes.modifications() != null) {
                            spread += spellAttributes.modifications().spread();
                        }
                        if (spread < 0) {
                            spread = 0;
                        }
                        chain.cast(serverLevel, pos, direction, spread, speedModifier, livingEntity, EntitySpawnReason.SPAWN_ITEM_USE);
                    }
                });
            }
        }
    }

    private static @NonNull Vec3 getCastPosition(@NonNull LivingEntity entity) {
        Vec3 eyePos = entity.getEyePosition();
        double offsetY = Math.max(-0.25, entity.getY() + 0.1 - eyePos.y);
        return eyePos.add(0, offsetY, 0);
    }
}
