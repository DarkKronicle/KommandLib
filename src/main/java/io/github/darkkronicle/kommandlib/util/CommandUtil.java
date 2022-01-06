package io.github.darkkronicle.kommandlib.util;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import lombok.experimental.UtilityClass;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class CommandUtil {

    public <T> Optional<T> getArgument(CommandContext<ServerCommandSource> context, String name, Class<T> clazz) {
        try {
            return Optional.of(context.getArgument(name, clazz));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public <S> Optional<CommandNode<S>> getChild(CommandNode<S> node, String... keys) {
        CommandNode<S> child = node.getChild(keys[0]);
        if (child == null) {
            return Optional.empty();
        }
        if (keys.length == 1) {
            return Optional.of(child);
        }
        String[] otherKeys = Arrays.copyOfRange(keys, 1, keys.length);
        return getChild(child, otherKeys);
    }

    public LiteralArgumentBuilder<ServerCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

}
