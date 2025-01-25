package snekker.jetpack.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import snekker.jetpack.client.event.ClientEvents;
import snekker.jetpack.item.JetpackItem;

@Environment(EnvType.CLIENT)
public class JetpackClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        JetpackKeys.registerKeybinds();
        ClientEvents.registerEvents();
        HudRenderCallback.EVENT.register(JetpackClient::onHudRender);
    }

    private static void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        var client = MinecraftClient.getInstance();
        var player = client.player;
        if (player != null) {
            var jetpack = JetpackItem.getEquippedJetpack(player);
            var fuel = jetpack.getOrDefault(JetpackItem.JETPACK_FUEL, 0);
            drawContext.drawItem(jetpack, 8, drawContext.getScaledWindowHeight() / 2 - 8);
            if (JetpackItem.getActive(jetpack)) {
                drawContext.drawText(client.textRenderer, Text.literal(fuel + "/5000"), 30, drawContext.getScaledWindowHeight() / 2 - client.textRenderer.fontHeight / 2, 0xffffff, true);
            }
        }
    }
}
