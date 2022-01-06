package io.github.darkkronicle.kommandlib.interfaces;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.command.ServerCommandSource;

public interface ICommandDispatcher {

    CommandDispatcher<ServerCommandSource> getClientDispatcher();

    void invoke(StringReader reader, String message);

    boolean isClientCommand(StringReader reader, String message);

    <S> boolean isClientCommand(CommandNode<S> command);
}
