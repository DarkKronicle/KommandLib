package io.github.darkkronicle.kommandlib.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

public interface ClientCommand extends Command<ServerCommandSource> {

    static ClientCommand of(ClientCommand command) {
        return command;
    }

    @Override
    default int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        runCommand(context);
        return 0;
    }

    void runCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException;

}
