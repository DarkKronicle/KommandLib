package io.github.darkkronicle.kommandlib.mixin;

import com.mojang.brigadier.CommandDispatcher;
import io.github.darkkronicle.kommandlib.CommandManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow private CommandDispatcher<ServerCommandSource> commandDispatcher;

    @Inject(
            method = "onCommandTree",
            at = @At("RETURN")
    )
    private void onCommandTree(CommandTreeS2CPacket packet, CallbackInfo ci) {
        CommandManager.getInstance().setServerDispatcher(commandDispatcher);
    }

}
