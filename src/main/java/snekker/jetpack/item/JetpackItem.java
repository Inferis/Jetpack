package snekker.jetpack.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import snekker.jetpack.item.component.JetpackComponents;

public class JetpackItem extends Item {
    public JetpackItem(Settings settings) {
        super(settings);
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


}
