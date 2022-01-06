package io.github.darkkronicle.kommandlib;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.darkkronicle.kommandlib.command.ArgumentClientCommand;
import io.github.darkkronicle.kommandlib.invokers.BaseCommandInvoker;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import io.github.darkkronicle.kommandlib.util.InfoUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.CLIENT)
public class KommandLib implements ClientModInitializer {

    public final static String MOD_ID = "kommandlib";

    @Override
    public void onInitializeClient() {
        CommandManager.getInstance().addCommand(new BaseCommandInvoker(
                MOD_ID,
                "kommandlib",
                CommandUtil.literal("kommandlib").then(
                        ArgumentClientCommand.toCommand((context, value) -> InfoUtil.sendChatMessage(value + ""), Integer.class, IntegerArgumentType.integer())
                ).build()
        ));
    }

}
