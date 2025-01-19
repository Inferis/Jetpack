package snekker.jetpack.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import snekker.jetpack.Jetpack;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.networking.SetJetpackActivePayload;
import snekker.jetpack.util.JetpackUtil;

@Environment(EnvType.CLIENT)
public class JetpackKeys {
    private static KeyBinding toggleBinding;

    public static void registerKeybinds() {
        registerToggleActiveKeybind();
    }

    private static void registerToggleActiveKeybind() {
        toggleBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "jetpack.key.toggle_active",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_J,
                "jetpack.category.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            var pressed = toggleBinding.wasPressed();
            if (pressed) {
                var player = client.player;
                if (player != null) {
                    var jetpackStack = JetpackUtil.getEquippedJetpack(player);
                    if (!jetpackStack.isEmpty()) {
                        JetpackItem.toggleActive(jetpackStack);
                        var active = JetpackItem.getActive(jetpackStack);
                        Jetpack.LOGGER.info("active(client)=" + active);
                        player.getAbilities().allowFlying = active;
                        player.getAbilities().flying = active;
                        if (active) {
                            player.move(MovementType.PLAYER, new Vec3d(0, 0.5, 0));
                        }
                        if (ClientPlayNetworking.canSend(SetJetpackActivePayload.ID)) {
                            ClientPlayNetworking.send(new SetJetpackActivePayload(active));
                        }
                    }
                }
            }
        });
    }
}
