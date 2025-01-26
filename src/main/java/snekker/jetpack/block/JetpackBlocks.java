package snekker.jetpack.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import snekker.jetpack.Jetpack;

public class JetpackBlocks {
    public static RechargerBlock RECHARGER;

    interface BlockMaker<T extends Block> {
        T makeBlock(RegistryKey<Block> key);
    }

    private static <T extends Block> T registerBlock(String name, BlockMaker<T> blockMaker) {
        Jetpack.LOGGER.info("Registering block: " + Jetpack.MODID + ":" + name);

        var identifier = Jetpack.id(name);
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        return Registry.register(
                Registries.BLOCK,
                identifier,
                blockMaker.makeBlock(key));
    }

    public static void registerBlocks() {
        RECHARGER = registerBlock("recharger", key -> {
            return new RechargerBlock(AbstractBlock.Settings.copy(Blocks.STONE).registryKey(key));
        });
    }
}