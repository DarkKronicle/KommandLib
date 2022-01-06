package io.github.darkkronicle.kommandlib.invokers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.darkkronicle.kommandlib.command.CommandInvoker;
import net.minecraft.server.command.ServerCommandSource;


public class BaseCommandInvoker extends CommandInvoker<ServerCommandSource> {

    private final String name;
    private final String modId;

    /**
     *
     * @param name
     * @param source {@link LiteralCommandNode) for the source
     */
    public BaseCommandInvoker(String modId, String name, CommandNode<ServerCommandSource> source) {
        super(source.getName(), source.getCommand(), source.getRequirement(), source.getRedirect(), source.getRedirectModifier(), source.isFork());
        for (CommandNode<ServerCommandSource> child : source.getChildren()) {
            this.addChild(child);
        }
        this.name = name.toLowerCase();
        this.modId = modId;
    }

    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public boolean isConflict(CommandInvoker<ServerCommandSource> invoker) {
        if (invoker instanceof BaseCommandInvoker) {
            return invoker.getName().equals(name);
        }
        return invoker.equals(this);
    }

    @Override
    public void registerToDispatcher(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.getRoot().addChild(this);
    }

    @Override
    public boolean isCommand(StringReader reader, String message) {
        return reader.getRemaining().toLowerCase().startsWith(name);
    }

    @Override
    public String getName() {
        return name;
    }

}
