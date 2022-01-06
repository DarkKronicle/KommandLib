package io.github.darkkronicle.kommandlib.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@UtilityClass
public class InfoUtil {

    public void sendChatMessage(String message, Formatting formatting) {
        sendChatMessage(TextUtil.ofFormatting(message, formatting));
    }

    public void sendChatMessage(String message) {
        sendChatMessage(message, Formatting.GRAY);
    }

    public void sendChatMessage(Text text) {
        MinecraftClient.getInstance().player.sendMessage(text, false);
    }

}
