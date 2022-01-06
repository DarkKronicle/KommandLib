package io.github.darkkronicle.kommandlib;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import io.github.darkkronicle.kommandlib.command.ClientCommandSource;
import io.github.darkkronicle.kommandlib.interfaces.ICommandDispatcher;
import io.github.darkkronicle.kommandlib.command.CommandInvoker;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import io.github.darkkronicle.kommandlib.util.InfoUtil;
import io.github.darkkronicle.kommandlib.util.TextUtil;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import javax.management.RuntimeErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class CommandManager implements ICommandDispatcher {

    private final static CommandManager INSTANCE = new CommandManager();

    private final Map<String, CommandInvoker<ServerCommandSource>> commands = new HashMap<>();
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Getter
    private CommandDispatcher<ServerCommandSource> clientDispatcher;
    private CommandDispatcher<ServerCommandSource> serverDispatcher;

    public static CommandManager getInstance() {
        return INSTANCE;
    }

    private CommandManager() {
        setupInternal();
    }

    public RootCommandNode<ServerCommandSource> getRoot() {
        return clientDispatcher.getRoot();
    }

    public void setServerDispatcher(CommandDispatcher<ServerCommandSource> server) {
        serverDispatcher = server;
        for (CommandNode<ServerCommandSource> command : clientDispatcher.getRoot().getChildren()) {
            serverDispatcher.getRoot().addChild(command);
        }
    }

    public void setupInternal() {
        clientDispatcher = new CommandDispatcher<>();
        for (CommandInvoker<ServerCommandSource> invoker : commands.values()) {
            invoker.registerToDispatcher(this.clientDispatcher);
        }
    }

    public Optional<CommandNode<ServerCommandSource>> getChild(String... keys) {
        CommandInvoker<ServerCommandSource> invoker = getInvoker(keys[0]).orElse(null);
        if (invoker == null) {
            return Optional.empty();
        }
        if (keys.length == 1) {
            return Optional.of(invoker);
        }
        return CommandUtil.getChild(invoker, Arrays.copyOfRange(keys, 1, keys.length));
    }

    public Optional<CommandInvoker<ServerCommandSource>> getInvoker(String key) {
        CommandInvoker<ServerCommandSource> invoker = commands.get(key);
        if (invoker == null) {
            return Optional.empty();
        }
        return Optional.of(invoker);
    }

    private boolean checkAllowed(CommandInvoker<ServerCommandSource> invoker) {
        for (CommandInvoker<ServerCommandSource> command : commands.values()) {
            if (command.isConflict(invoker)) {
                return false;
            }
        }
        return true;
    }

    public void addCommand(CommandInvoker<ServerCommandSource> invoker) {
        if (checkAllowed(invoker)) {
            commands.put(invoker.getName(), invoker);
            invoker.registerToDispatcher(this.clientDispatcher);
            if (serverDispatcher != null) {
                invoker.registerToDispatcher(this.serverDispatcher);
            }
        }
    }

    public void unregister(Predicate<CommandInvoker<ServerCommandSource>> predicate) {
        List<CommandInvoker<ServerCommandSource>> toRemove = new ArrayList<>();
        for (CommandInvoker<ServerCommandSource> invoker : commands.values()) {
            if (predicate.test(invoker)) {
                toRemove.add(invoker);
            }
        }
        for (CommandInvoker<ServerCommandSource> invoker : toRemove) {
            unregister(invoker);
        }
    }

    public void unregister(String name) {
        CommandInvoker<ServerCommandSource> invoker = getInvoker(name).orElse(null);
        if (invoker == null) {
            return;
        }
        unregister(invoker);
    }

    public void unregister(CommandInvoker<ServerCommandSource> invoker) {
        commands.remove(invoker.getName());
        clientDispatcher.getRoot().getChildren().remove(invoker);
        serverDispatcher.getRoot().getChildren().remove(invoker);
    }

    @Override
    public void invoke(StringReader reader, String message) {
        try {
            this.getClientDispatcher().execute(reader, new ClientCommandSource(client.player));
        } catch (CommandSyntaxException e) {
            handleCommandSyntaxException(reader, message, e);
        } catch (RuntimeErrorException e) {
            handleRunTimeException(reader, message, e);
        } catch (Exception e) {
            handleException(reader, message, e);
        }
    }

    @Override
    public boolean isClientCommand(StringReader reader, String message) {
        for (CommandInvoker<ServerCommandSource> invoker : commands.values()) {
            if (invoker.isCommand(reader, message)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <S> boolean isClientCommand(CommandNode<S> command) {
        if (!(command instanceof LiteralCommandNode<S>)) {
            return false;
        }
        for (CommandInvoker<ServerCommandSource> invoker : commands.values()) {
            if (invoker.getLiteral().equals(((LiteralCommandNode<S>) command).getLiteral())) {
                return true;
            }
        }
        return false;
    }

    private void handleCommandSyntaxException(StringReader reader, String message, CommandSyntaxException e) {
        String errorMessage = e.getRawMessage().getString();
        String context = e.getContext();

        InfoUtil.sendChatMessage(TextUtil.concat(
                TextUtil.ofFormatting(errorMessage, Formatting.RED),
                TextUtil.ofFormatting("\nAt position ", Formatting.GRAY),
                TextUtil.ofFormatting(e.getCursor() + " - ", Formatting.RED),
                TextUtil.ofFormatting(context, Formatting.GRAY)
        ));
    }

    private void handleRunTimeException(StringReader reader, String message, RuntimeException e) {
        InfoUtil.sendChatMessage(TextUtil.concat(
                TextUtil.ofFormatting("There was a runtime error executing this command.\n", Formatting.RED),
                TextUtil.ofFormatting(e.getMessage(), Formatting.GRAY)
        ));
    }

    private void handleException(StringReader reader, String message, Exception e) {
        InfoUtil.sendChatMessage(TextUtil.concat(
                TextUtil.ofFormatting("There was an internal error executing this command. ", Formatting.RED),
                TextUtil.ofFormatting(e.getMessage(), Formatting.GRAY)
        ));
    }

}
