package io.github.darkkronicle.kommandlib.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.function.Predicate;

public abstract class CommandInvoker<S> extends LiteralCommandNode<S> {

    public CommandInvoker(String literal, Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks) {
        super(literal, command, requirement, redirect, modifier, forks);
    }

    public abstract String getModId();

    public abstract boolean isConflict(CommandInvoker<S> invoker);

    public abstract void registerToDispatcher(CommandDispatcher<S> dispatcher);

    public abstract boolean isCommand(StringReader reader, String message);

}
