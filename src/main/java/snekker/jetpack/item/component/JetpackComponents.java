package snekker.jetpack.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import snekker.jetpack.Jetpack;

public class JetpackComponents {
    public static final ComponentType<Boolean> JETPACK_ACTIVE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Jetpack.id("jetpack_active"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );

    public static void registerComponents() {
    }
}
