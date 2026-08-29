package dynastxu.noitacore;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dynastxu.noitacore.attachment.UnlockedSpells;
import dynastxu.noitacore.item.Items;
import dynastxu.noitacore.item.SpellItem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static dynastxu.noitacore.NoitaCore.MODID;

@EventBusSubscriber(modid = MODID)
public final class ModCommands {
    private static final SimpleCommandExceptionType ERROR_ARGUMENT_INVALID = new SimpleCommandExceptionType(translatable("argument_invalid"));

    @SubscribeEvent
    public static void registerCommands(@NonNull RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal(MODID)
                .requires(source -> source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.ADMINS)))
                .then(Commands.literal("unlock")
                        .then(Commands.literal("all")
                                .then(Commands.literal("spell")
                                        .executes(ModCommands::unlockAllSpells)
                                )
                        )
                        .then(Commands.argument("spell", IdentifierArgument.id())
                                .suggests(ModCommands::spellSuggestions)
                                .executes(ModCommands::unlockSpell)
                        )
                )
                .then(Commands.literal("lock")
                        .then(Commands.literal("all")
                                .then(Commands.literal("spell")
                                        .executes(ModCommands::lockAllSpells)
                                )
                        )
                        .then(Commands.argument("spell", IdentifierArgument.id())
                                .suggests(ModCommands::spellSuggestions)
                                .executes(ModCommands::lockSpell)
                        )
                )
        );
    }

    private static int lockSpell(@NonNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        Identifier spell = IdentifierArgument.getId(context, "spell");
        Item item = BuiltInRegistries.ITEM.get(spell).orElseThrow(ERROR_ARGUMENT_INVALID::create).value();
        if (!(item instanceof SpellItem)) {
            context.getSource().sendFailure(translatable("argument_invalid"));
            return 0;
        }
        UnlockedSpells.lock(player, BuiltInRegistries.ITEM.wrapAsHolder(item));
        context.getSource().sendSuccess(
                () -> translatable("spell_locked").append(Component.translatable(item.getDescriptionId())),
                false
        );
        return 1;
    }

    private static int unlockSpell(@NonNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        Identifier spell = IdentifierArgument.getId(context, "spell");
        Item item = BuiltInRegistries.ITEM.get(spell).orElseThrow(ERROR_ARGUMENT_INVALID::create).value();
        if (!(item instanceof SpellItem)) {
            context.getSource().sendFailure(translatable("argument_invalid"));
            return 0;
        }
        UnlockedSpells.unlock(player, BuiltInRegistries.ITEM.wrapAsHolder(item));
        context.getSource().sendSuccess(
                () -> translatable("spell_unlocked").append(Component.translatable(item.getDescriptionId())),
                false
        );
        return 1;
    }

    private static int lockAllSpells(@NonNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        UnlockedSpells.lockAll(player);
        context.getSource().sendSuccess(
                () -> translatable("spell_lock_all"),
                false
        );
        return 1;
    }

    private static int unlockAllSpells(@NonNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        UnlockedSpells.unlockAll(player);
        context.getSource().sendSuccess(
                () -> translatable("spell_unlock_all"),
                false
        );
        return 1;
    }

    private static @NonNull CompletableFuture<Suggestions> spellSuggestions(final CommandContext<CommandSourceStack> context, final SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(
                Items.SPELL_ITEMS.stream()
                        .map(item -> BuiltInRegistries.ITEM.getKey(item.get()).toString()),
                builder
        );
    }

    private static @NonNull MutableComponent translatable(String key) {
        return Component.translatable("command." + MODID + "." + key);
    }
}
