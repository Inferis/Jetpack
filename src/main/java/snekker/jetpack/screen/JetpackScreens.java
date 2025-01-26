package snekker.jetpack.screen;

import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import snekker.jetpack.Jetpack;

public class JetpackScreens {
    public static ScreenHandlerType<RechargerScreenHandler> RECHARGER = Registry.register(
            Registries.SCREEN_HANDLER,
            Jetpack.id("recharger"),
            new ScreenHandlerType<>(RechargerScreenHandler::new, FeatureSet.empty()));

    public static void registerScreenHandlers() {
    }
}
