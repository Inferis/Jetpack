package snekker.jetpack.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.networking.SetJetpackActiveC2SPayload;
import snekker.jetpack.util.JetpackUtil;

@Environment(EnvType.CLIENT)
public class JetpackKeys {
    private static KeyBinding toggleBinding;
    private static KeyBinding powerBinding;

    public static void registerKeybinds() {
        registerToggleActiveKeybind();
    }

    private static void registerToggleActiveKeybind() {
        toggleBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "jetpack.key.hover",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_H,
                "jetpack.category.keys"
        ));
        powerBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "jetpack.key.fly",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_J,
                "jetpack.category.keys"
        ));

        ClientTickEvents.START_CLIENT_TICK.register(JetpackKeys::onClientTick);
    }

    private static void onClientTick(MinecraftClient client) {
        if (toggleBinding.wasPressed()) {
            toggleJetpackActive(client);
        }

        if (client.player != null && !client.player.getAbilities().flying) {
            var jetpack = JetpackItem.getEquippedJetpack(client.player);
            if (!jetpack.isEmpty() && jetpack.getOrDefault(JetpackItem.JETPACK_FUEL, 0) > 0) {
                if (powerBinding.isPressed()) {
                    JetpackItem.setActive(jetpack, true);
                    var vec = client.player.getRotationVector();
                    vec = vec.add(0, 0.15, 0);
                    client.player.setVelocity(vec);
                    if (ClientPlayNetworking.canSend(SetJetpackActiveC2SPayload.ID)) {
                        ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(true));
                    }
                }
                else if (JetpackItem.getActive(jetpack)) {
                    JetpackItem.setActive(jetpack, false);
                    if (ClientPlayNetworking.canSend(SetJetpackActiveC2SPayload.ID)) {
                        ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(false));
                    }
                }
            }
        }
    }

    private static void toggleJetpackActive(MinecraftClient client) {
        var player = client.player;
        if (player != null && !player.isSpectator() && !player.isInCreativeMode()) {
            var jetpackStack = JetpackItem.getEquippedJetpack(player);
            if (!jetpackStack.isEmpty()) {
                if (JetpackItem.getActive(jetpackStack) || jetpackStack.getOrDefault(JetpackItem.JETPACK_FUEL, 0) > 0) {
                    JetpackItem.toggleActive(jetpackStack);
                    var active = JetpackItem.getActive(jetpackStack);
                    JetpackUtil.setFlying(player, active);
                    //player.getAbilities().setFlySpeed(0.25f);

                    if (player.isOnGround() && active) {
                        player.setVelocity(new Vec3d(0, 0.5, 0));
                    }

                    ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(active));
                }
            }
        }
    }
}
