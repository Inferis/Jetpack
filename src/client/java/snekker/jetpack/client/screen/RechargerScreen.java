package snekker.jetpack.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import snekker.jetpack.Jetpack;
import snekker.jetpack.screen.RechargerScreenHandler;

public class RechargerScreen extends HandledScreen<RechargerScreenHandler> {
    public static final Identifier BACKGROUND_TEXTURE = Jetpack.id("textures/gui/container/recharger.png");
    public static final Identifier CHARGE_PROGRESS_TEXTURE = Jetpack.id("container/recharger/charge_progress");

    public RechargerScreen(RechargerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    private int originX;
    private int originY;

    @Override
    protected void init() {
        super.init();

        originX = (width - backgroundWidth) / 2;
        originY = (height - backgroundHeight) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, originX, originY, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
        if (handler.isCharging()) {
            int progress = MathHelper.ceil(this.handler.getChargeProgress() * 13.0F) + 1;
            context.drawGuiTexture(RenderLayer::getGuiTextured, CHARGE_PROGRESS_TEXTURE, 14, 14, 0, 14 - progress, originX + 82, originY + 56 + 14 - progress, 14, progress);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        var text = handler.getJetpackFuelStat();
        var width = getTextRenderer().getWidth(text);
        context.drawText(getTextRenderer(), text, originX + 76- width, originY + 29, 0, false);

//        text = handler.getFuelStat();
//        width = getTextRenderer().getWidth(text);
//        context.drawText(getTextRenderer(), text, originX + 76 - width, originY + 35, 0, false);
    }
}
