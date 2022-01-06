package io.github.darkkronicle.kommandlib.builders;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import io.github.darkkronicle.kommandlib.command.ClientCommand;
import io.github.darkkronicle.kommandlib.command.InputClientCommand;
import io.github.darkkronicle.kommandlib.interfaces.ICommandBuilder;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import net.minecraft.server.command.ServerCommandSource;

public class InputArgumentBuilder implements ICommandBuilder {
    @Override
    public CommandNode<ServerCommandSource> build(String name, ClientCommand command) {
        return CommandUtil.argument("input", StringArgumentType.greedyString()).executes(command).build();
    }

    public static CommandNode<ServerCommandSource> input(InputClientCommand command) {
        return new InputArgumentBuilder().build("", command);
    }
}
