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
                "jetpack.key.toggle_active",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_J,
                "jetpack.category.keys"
        ));
        powerBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "jetpack.key.power",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_K,
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
            if (!jetpack.isEmpty()) {
                if (powerBinding.isPressed()) {
                    JetpackItem.setActive(jetpack, true);
                    client.player.setVelocity(new Vec3d(0, 0.25, 0));
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
                JetpackItem.toggleActive(jetpackStack);
                var active = JetpackItem.getActive(jetpackStack);
                JetpackUtil.setFlying(player, active);
                //player.getAbilities().setFlySpeed(0.25f);

                var inAir = false;
                if (client.world != null) {
                    var pos = player.getBlockPos().offset(Direction.DOWN, 1);
                    var blockState = client.world.getBlockState(pos);
                    inAir = blockState.getBlock() == Blocks.AIR;
                }
                if (!inAir && active) {
                    player.setVelocity(new Vec3d(0, 0.5, 0));
                }

                if (ClientPlayNetworking.canSend(SetJetpackActiveC2SPayload.ID)) {
                    ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(active));
                }
            }
        }
    }
}
