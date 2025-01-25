package snekker.jetpack.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import snekker.jetpack.Jetpack;

public class JetpackItems {
    public static JetpackItem JETPACK;

    interface ItemMaker<T extends Item> {
        T makeItem(RegistryKey<Item> key);
    }

    private static <T extends Item> T registerItem(String name, ItemMaker<T> itemMaker) {
        Jetpack.LOGGER.info("Registering item: " + Jetpack.MODID + ":" + name);

        var identifier = Jetpack.id(name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, identifier);
        T item = itemMaker.makeItem(key);
        return Registry.register(Registries.ITEM, identifier, item);
    }

    public static void registerItems() {
        JETPACK = registerItem("jetpack", key -> {
            return new JetpackItem(new Item.Settings().useItemPrefixedTranslationKey().registryKey(key));
        });
    }
}
