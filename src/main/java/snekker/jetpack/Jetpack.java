package snekker.jetpack;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import snekker.jetpack.item.JetpackItems;
import snekker.jetpack.event.ServerEvents;
import snekker.jetpack.networking.JetpackNetworking;

public class Jetpack implements ModInitializer {
    public static final String MODID = "jetpack";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static Identifier id(String id) {
        return Identifier.of(MODID, id);
    }

    @Override
    public void onInitialize() {
        JetpackItems.registerItems();
        JetpackNetworking.registerPayloads();
        ServerEvents.registerServerEvents();
    }
}
