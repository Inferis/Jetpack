package snekker.jetpack.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.networking.SetJetpackActiveC2SPayload;
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
                if (player != null && !player.isSpectator() && !player.isInCreativeMode()) {
                    var jetpackStack = JetpackItem.getEquippedJetpack(player);
                    if (!jetpackStack.isEmpty()) {
                        JetpackItem.toggleActive(jetpackStack);
                        var active = JetpackItem.getActive(jetpackStack);
                        JetpackUtil.setFlying(player, active);
                        var inAir = false;
                        if (client.world != null) {
                            var pos = player.getBlockPos().offset(Direction.DOWN, 1);
                            var blockState = client.world.getBlockState(pos);
                            inAir = blockState.getBlock() == Blocks.AIR;
                        }
                        if (!inAir && active) {
                            player.move(MovementType.PLAYER, new Vec3d(0, 1.0, 0));
                        }
                        if (ClientPlayNetworking.canSend(SetJetpackActiveC2SPayload.ID)) {
                            ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(active));
                        }
                    }
                }
            }
        });
    }
}
