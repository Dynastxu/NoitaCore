package dynastxu.noitacore.item;

import dynastxu.noitacore.DataMaps;
import dynastxu.noitacore.client.font.Font;
import dynastxu.noitacore.common.spell.DamageType;
import dynastxu.noitacore.common.spell.SpellAttributes;
import dynastxu.noitacore.components.DataComponents;
import dynastxu.noitacore.components.SpellData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public class ToolTips {
    @SubscribeEvent
    public static void addTooltips(@NonNull ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();
        if (stack.getItem() instanceof SpellItem spellItem) {
            SpellData spellData = stack.get(DataComponents.SPELL_DATA);
            SpellAttributes spellAttributes = stack.getData(DataMaps.SPELL_ATTRIBUTES);
            tooltip.add(Component.translatable(spellItem.getDescriptionId() + ".description").withStyle(ChatFormatting.BLUE));
            if (spellAttributes != null) {
                tooltip.add(Component.literal(" "));
                // 类型
                tooltip.add(Component.literal(Font.TYPE + " ")
                        .append(Component.translatable("tooltip.noitacore.spell_type"))
                        .append("    ")
                        .append(spellAttributes.base().type().getTranslatedName()));
                // 使用次数
                if (spellAttributes.base().uses().uses() > 0) {
                    var c = Component.literal(Font.USES + " ")
                            .append(Component.translatable("tooltip.noitacore.uses"))
                            .append("    ");
                    if (spellData != null) {
                        var v = Component.literal(String.valueOf(spellData.remainingUses()));

                        if (spellData.remainingUses() == 0) {
                            v.withStyle(ChatFormatting.RED);
                        } else if (spellData.remainingUses() < spellAttributes.base().uses().uses()) {
                            v.withStyle(ChatFormatting.YELLOW);
                        } else {
                            v.withStyle(ChatFormatting.GREEN);
                        }

                        c.append(v);
                    } else {
                        c.append(String.valueOf(spellAttributes.base().uses().uses()));
                    }
                    tooltip.add(c);
                }
                // 法力消耗
                tooltip.add(Component.literal(Font.MANA_DRAIN + " ")
                        .append(Component.translatable("tooltip.noitacore.mana_drain"))
                        .append("    ")
                        .append(String.valueOf(spellAttributes.base().manaDrain())));
                // 施放延迟
                tooltip.add(Component.literal(Font.CAST_DELAY + " ")
                        .append(Component.translatable("tooltip.noitacore.cast_delay"))
                        .append("    ")
                        .append(String.format("%.2f", (float) spellAttributes.base().castDelayTick() / 20)));
                // 充能时间
                tooltip.add(Component.literal(Font.RECHARGE + " ")
                        .append(Component.translatable("tooltip.noitacore.recharge_time"))
                        .append("    ")
                        .append(String.format("%.2f", (float) spellAttributes.base().rechargeTick() / 20)));
                // 散射
                if (spellAttributes.modifications() != null) {
                    tooltip.add(Component.literal(Font.SPREAD + " ")
                            .append(Component.translatable("tooltip.noitacore.spread_modification"))
                            .append("    ")
                            .append(String.format("%.2f", spellAttributes.modifications().spread())));
                }
                // 伤害
                if (spellAttributes.damage() != null) {
                    Font icon = switch (spellAttributes.damage().damageType()) {
                        case DamageType.Projectile -> Font.PROJECTILE;
                        default -> Font.NONE;
                    };
                    tooltip.add(Component.literal(icon + " ")
                            .append(Component.translatable("tooltip.noitacore.damage"))
                            .append("    ")
                            .append(String.format("%.2f", spellAttributes.damage().damage())));
                }
                // 半径
                if (spellAttributes.damage() != null && spellAttributes.damage().explosionRadius() > 0) {
                    tooltip.add(Component.literal(Font.RADIUS + " ")
                            .append(Component.translatable("tooltip.noitacore.radius"))
                            .append("    ")
                            .append(String.format("%.2f", spellAttributes.damage().explosionRadius())));
                }
                // 速度
                if (spellAttributes.motion() != null) {
                    tooltip.add(Component.literal(Font.SPEED + " ")
                            .append(Component.translatable("tooltip.noitacore.speed"))
                            .append("    ")
                            .append(String.format("%.2f", spellAttributes.motion().initialSpeed() * 20)));
                }
                // 暴击率
                if (spellAttributes.modifications() != null && spellAttributes.modifications().criticalChance() != 0) {
                    tooltip.add(Component.literal(Font.CRIT + " ")
                            .append(Component.translatable("tooltip.noitacore.crit"))
                            .append("    ")
                            .append(String.format("%.2f%%", spellAttributes.modifications().criticalChance() * 100)));
                }
            }
        }
    }
}
