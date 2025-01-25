package snekker.jetpack.item;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import snekker.jetpack.Jetpack;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

public class JetpackItem extends ArmorItem {
    static RegistryKey<EquipmentAsset> JETPACK_EQUIPMENT_ASSET_KEY = EquipmentAssetKeys.register("jetpack");
    static ArmorMaterial JETPACK_MATERIAL = new ArmorMaterial(15, Util.make(new EnumMap(EquipmentType.class), (map) -> {
        map.put(EquipmentType.CHESTPLATE, 1);
    }), 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, ItemTags.REPAIRS_IRON_ARMOR, JETPACK_EQUIPMENT_ASSET_KEY);

    public static final ComponentType<Boolean> JETPACK_ACTIVE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Jetpack.id("jetpack_active"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN).build()
    );
    public static final ComponentType<Integer> JETPACK_FUEL = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Jetpack.id("jetpack_fuel"),
            ComponentType.<Integer>builder().codec(Codec.INT).packetCodec(PacketCodecs.INTEGER).build()
    );

    public JetpackItem(Settings settings) {
        super(JETPACK_MATERIAL, EquipmentType.CHESTPLATE, settings
                .maxCount(1)
                .equippable(EquipmentSlot.CHEST)
                .component(JETPACK_FUEL, 5000)
        );
    }

    public int getItemBarStep(ItemStack stack) {
        var fuel = stack.get(JETPACK_FUEL);
        Jetpack.LOGGER.info("fuel step = " + fuel);
        return MathHelper.clamp(Math.round((float)fuel * 13.0F / 5000.0F), 0, 13);
    }

    public int getItemBarColor(ItemStack stack) {
        return 0x11c9e9;
    }

    public boolean isItemBarVisible(ItemStack stack) {
        var fuel = stack.get(JETPACK_FUEL);
        return fuel < 5000;
    }

        @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.literal("Fuel: " + stack.get(JETPACK_FUEL) + " of 5000"));
    }

    public static void toggleActive(ItemStack stack) {
        setActive(stack, !getActive(stack));
    }

    public static boolean getActive(ItemStack stack) {
        return stack.getOrDefault(JETPACK_ACTIVE, false);
    }

    public static void setActive(ItemStack stack, boolean active) {
        Jetpack.LOGGER.info("fuel1 = " + stack.get(JetpackItem.JETPACK_FUEL));
        if (active) {
            stack.set(JETPACK_ACTIVE, true);
            Jetpack.LOGGER.info("fuel2 = " + stack.get(JetpackItem.JETPACK_FUEL));
        }
        else {
            stack.set(JETPACK_ACTIVE, null);
            Jetpack.LOGGER.info("fuel3 = " + stack.get(JetpackItem.JETPACK_FUEL));
        }
    }

    public static ItemStack getEquippedJetpack(PlayerEntity player) {
        var chest = player.getEquippedStack(EquipmentSlot.CHEST);
        if (chest.isOf(JetpackItems.JETPACK)) {
            return chest;
        }
        else {
            return ItemStack.EMPTY;
        }
    }

    public static Optional<Integer> consumeFuel(ItemStack jetpackStack) {
        var fuel = jetpackStack.get(JetpackItem.JETPACK_FUEL);
        if (fuel > 0) {
            fuel -= 1;
            jetpackStack.set(JetpackItem.JETPACK_FUEL, fuel);
            return Optional.of(fuel);
        }
        return Optional.empty();
    }
}
