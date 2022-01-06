package io.github.darkkronicle.kommandlib.command;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.stream.Collectors;

/*
 * Original author Earthcomputer https://github.com/Earthcomputer/clientcommands/blob/fabric/src/main/java/net/earthcomputer/clientcommands/command/FakeCommandSource.java
 *
 * This is under the license https://www.gnu.org/licenses/lgpl-3.0.en.html
 */
public class ClientCommandSource extends ServerCommandSource {

    public ClientCommandSource(ClientPlayerEntity player) {
        super(player, player.getPos(), player.getRotationClient(), null, 0, player.getEntityName(), player.getName(), null, player);
    }

    @Override
    public Collection<String> getPlayerNames() {
        return MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(e -> e.getProfile().getName()).collect(Collectors.toList());
    }

}
