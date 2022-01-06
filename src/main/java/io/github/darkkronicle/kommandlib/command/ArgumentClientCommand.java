package io.github.darkkronicle.kommandlib.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import lombok.Getter;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ArgumentClientCommand<T> implements ClientCommand {

    @Getter
    private final String key;
    @Getter
    private final ArgumentClientConsumer<T> consumer;
    @Getter
    private final Class<T> clazz;

    public ArgumentClientCommand(String key, Class<T> clazz, ArgumentClientConsumer<T> consumer) {
        this.key = key;
        this.consumer = consumer;
        this.clazz = clazz;
    }

    @Override
    public void runCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Optional<T> argument = CommandUtil.getArgument(context, key, clazz);
        consumer.runCommand(context, argument.orElse(null));
    }

    public interface ArgumentClientConsumer<T> {

        void runCommand(CommandContext<ServerCommandSource> context, @Nullable T value);

    }

    public static <T> CommandNode<ServerCommandSource> toSubCommand(String name, ArgumentClientConsumer<T> consumer, Class<T> clazz, ArgumentType<T> type) {
        ArgumentClientCommand<T> command = new ArgumentClientCommand<>("value", clazz, consumer);
        return toSubCommand(name, command, type);
    }

    public static <T> CommandNode<ServerCommandSource> toSubCommand(String commandName, ArgumentClientCommand<T> command, ArgumentType<T> type) {
        return CommandUtil.literal(commandName).executes(command).then(toCommand(command, type)).build();
    }

    public static <T> CommandNode<ServerCommandSource> toCommand(ArgumentClientConsumer<T> consumer, Class<T> clazz, ArgumentType<T> type) {
        ArgumentClientCommand<T> command = new ArgumentClientCommand<>("value", clazz, consumer);
        return toCommand(command, type);
    }

    public static <T> CommandNode<ServerCommandSource> toCommand(ArgumentClientCommand<T> consumer, ArgumentType<T> type) {
        return CommandUtil.argument(consumer.getKey(), type).executes(consumer).build();
    }

}
