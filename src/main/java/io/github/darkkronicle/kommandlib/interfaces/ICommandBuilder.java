package io.github.darkkronicle.kommandlib.interfaces;

import com.mojang.brigadier.tree.CommandNode;
import io.github.darkkronicle.kommandlib.command.ClientCommand;
import net.minecraft.server.command.ServerCommandSource;

public interface ICommandBuilder {

    CommandNode<ServerCommandSource> build(String name, ClientCommand command);

}
