package snekker.jetpack.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import snekker.jetpack.client.event.ClientEvents;

@Environment(EnvType.CLIENT)
public class JetpackClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        JetpackKeys.registerKeybinds();
        ClientEvents.registerEvents();
    }
}
