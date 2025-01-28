package snekker.jetpack.client;

import com.google.common.collect.Maps;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyBindingRegistry {
    public static final Map<InputUtil.Key, List<KeyBinding>> KEY_TO_BINDINGS = Maps.newHashMap();

    public static KeyBinding register(KeyBinding keyBinding) {
        var key = keyBinding.getDefaultKey();
        var keyBindings = KEY_TO_BINDINGS.computeIfAbsent(key, k -> new ArrayList<>());
        keyBindings.add(keyBinding);
        return keyBinding;
    }

    public static List<KeyBinding> get(InputUtil.Key key) {
        var keyBindings = KEY_TO_BINDINGS.get(key);
        if (keyBindings != null) {
            return keyBindings;
        }
        else {
            return new ArrayList<>();
        }
    }
}
