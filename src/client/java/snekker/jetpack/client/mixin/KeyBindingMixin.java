package snekker.jetpack.client.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snekker.jetpack.client.KeyBindingRegistry;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Shadow
    private int timesPressed;

    @Inject(at=@At("HEAD"), method="setKeyPressed")
    private static void setKeyPressed(InputUtil.Key key, boolean pressed, CallbackInfo ci) {
        for (var keyBinding: KeyBindingRegistry.get(key)) {
            keyBinding.setPressed(pressed);
        }
    }

    @Inject(at=@At("HEAD"), method="onKeyPressed")
    private static void onKeyPressed(InputUtil.Key key, CallbackInfo ci) {
        for (var keyBinding: KeyBindingRegistry.get(key)) {
            ((KeyBindingMixin)(Object)keyBinding).timesPressed++;
        }
    }

}
