package io.github.darkkronicle.kommandlib.builders;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.github.darkkronicle.kommandlib.command.ClientCommand;
import io.github.darkkronicle.kommandlib.command.InputClientCommand;
import io.github.darkkronicle.kommandlib.interfaces.ICommandBuilder;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import net.minecraft.server.command.ServerCommandSource;

public class InputCommandBuilder implements ICommandBuilder {

    @Override
    public CommandNode<ServerCommandSource> build(String name, ClientCommand command) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandUtil.literal(name);
        builder.executes(command).then(CommandUtil.argument("input", StringArgumentType.greedyString()).executes(command));
        return builder.build();
    }

    public static CommandNode<ServerCommandSource> fromInput(String name, InputClientCommand command) {
        InputCommandBuilder builder = new InputCommandBuilder();
        return builder.build(name, command);
    }
}
