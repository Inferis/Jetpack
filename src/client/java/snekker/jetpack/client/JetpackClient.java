package snekker.jetpack.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
//import net.minecraft.util.Identifier;
//import snekker.jetpack.item.JetpackItems;
//import snekker.jetpack.item.component.JetpackComponents;

@Environment(EnvType.CLIENT)
public class JetpackClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        JetpackKeys.registerKeybinds();
//        registerModelPredicateProviders();
    }

    public static void registerModelPredicateProviders() {
//        ModelPredicateProviderRegistry.register(JetpackItems.JETPACK, Identifier.ofVanilla("active"), (itemStack, clientWorld, livingEntity, seed) -> {
//            return itemStack.get(JetpackComponents.JETPACK_ACTIVE) ? 1 : 0;
//        });
    }
}
