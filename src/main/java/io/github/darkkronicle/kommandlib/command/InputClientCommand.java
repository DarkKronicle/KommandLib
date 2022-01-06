package io.github.darkkronicle.kommandlib.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import net.minecraft.server.command.ServerCommandSource;

public interface InputClientCommand extends ClientCommand {

    static InputClientCommand of(InputClientCommand command) {
        return command;
    }

    @Override
    default void runCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        run(context, CommandUtil.getArgument(context, "input", String.class).orElse(""));
    }

    void run(CommandContext<ServerCommandSource> context, String input) throws CommandSyntaxException;

}
