package io.github.darkkronicle.kommandlib.mixin;

import com.mojang.brigadier.StringReader;
import io.github.darkkronicle.kommandlib.CommandManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Inject(
            method = "sendChatMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if (message.startsWith("/")) {
            if (message.startsWith("/server:")) {
                this.networkHandler.sendPacket(new ChatMessageC2SPacket("/" + message.substring(8)));
                ci.cancel();
                return;
            }
            StringReader reader = new StringReader(message);
            reader.skip();
            if (!CommandManager.getInstance().isClientCommand(reader, message)) {
                return;
            }
            CommandManager.getInstance().invoke(reader, message);
            ci.cancel();
        }
    }

}
