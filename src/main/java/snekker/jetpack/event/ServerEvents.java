package snekker.jetpack.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class ServerEvents {
    public static void registerServerEvents() {
        ServerTickEvents.START_SERVER_TICK.register(ServerEvents::onServerTick);
    }

    private static void onServerTick(MinecraftServer minecraftServer) {
    }
}
