package snekker.jetpack.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import snekker.jetpack.item.component.JetpackComponents;

import java.util.EnumMap;
import java.util.Map;

public class JetpackItem extends ArmorItem {
    static RegistryKey<EquipmentAsset> JETPACK_EQUIPMENT_ASSET_KEY = EquipmentAssetKeys.register("jetpack");
    static ArmorMaterial JETPACK_MATERIAL = new ArmorMaterial(15, Util.make(new EnumMap(EquipmentType.class), (map) -> {
        map.put(EquipmentType.CHESTPLATE, 6);
    }), 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, ItemTags.REPAIRS_IRON_ARMOR, JETPACK_EQUIPMENT_ASSET_KEY);

    public JetpackItem(Settings settings) {
        super(JETPACK_MATERIAL, EquipmentType.CHESTPLATE, settings);
    }

    public static void toggleActive(ItemStack stack) {
        setActive(stack, !getActive(stack));
    }

    public static boolean getActive(ItemStack stack) {
        return stack.getOrDefault(JetpackComponents.JETPACK_ACTIVE, false);
    }

    public static void setActive(ItemStack stack, boolean active) {
        if (active) {
            stack.set(JetpackComponents.JETPACK_ACTIVE, true);
        }
        else {
            stack.set(JetpackComponents.JETPACK_ACTIVE, null);
        }
    }

    public static ItemStack getEquippedJetpack(PlayerEntity player) {
        var chest = player.getEquippedStack(EquipmentSlot.CHEST);
        if (chest.getItem() instanceof JetpackItem) {
            return chest;
        }
        else {
            return ItemStack.EMPTY;
        }
    }
}
