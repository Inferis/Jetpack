package snekker.jetpack;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import snekker.jetpack.block.JetpackBlockEntityTypes;
import snekker.jetpack.block.JetpackBlocks;
import snekker.jetpack.item.JetpackItems;
import snekker.jetpack.event.ServerEvents;
import snekker.jetpack.networking.JetpackNetworking;
import snekker.jetpack.screen.JetpackScreens;

public class Jetpack implements ModInitializer {
    public static final String MODID = "jetpack";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static Identifier id(String id) {
        return Identifier.of(MODID, id);
    }

    @Override
    public void onInitialize() {
        JetpackBlocks.registerBlocks();
        JetpackBlockEntityTypes.registerBlockEntityTypes();
        JetpackItems.registerItems();
        JetpackNetworking.registerPayloads();
        ServerEvents.registerServerEvents();
        JetpackScreens.registerScreenHandlers();
    }
}
