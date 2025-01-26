package snekker.jetpack.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import snekker.jetpack.client.event.ClientEvents;
import snekker.jetpack.client.screen.RechargerScreen;
import snekker.jetpack.screen.JetpackScreens;

@Environment(EnvType.CLIENT)
public class JetpackClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        JetpackKeys.registerKeybinds();
        ClientEvents.registerEvents();
        HandledScreens.register(JetpackScreens.RECHARGER, RechargerScreen::new);
    }
}
