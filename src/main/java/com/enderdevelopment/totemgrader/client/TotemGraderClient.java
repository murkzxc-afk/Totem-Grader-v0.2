package com.enderdevelopment.totemgrader.client;

import com.enderdevelopment.totemgrader.GradedTotemItem;
import com.enderdevelopment.totemgrader.TotemGraderMod;
import com.enderdevelopment.totemgrader.UpgradeScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ModelPredicateProviderRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public final class TotemGraderClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(
            TotemGraderMod.GRADED_TOTEM,
            TotemGraderMod.id("uses"),
            (stack, world, entity, seed) -> {
                return GradedTotemItem.uses(stack);
            }
        );
        HandledScreens.register(TotemGraderMod.UPGRADE_SCREEN_HANDLER, UpgradeScreen::new);
    }

    private static final class UpgradeScreen extends HandledScreen<UpgradeScreenHandler> {
        UpgradeScreen(UpgradeScreenHandler handler, PlayerInventory inventory, Text title) {
            super(handler, inventory, title);
            backgroundWidth = 194;
            backgroundHeight = 176;
        }

        @Override
        protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
            int x = (width - backgroundWidth) / 2;
            int y = (height - backgroundHeight) / 2;
            context.fill(x, y, x + backgroundWidth, y + backgroundHeight, 0xff292929);
            context.fill(x + 4, y + 4, x + backgroundWidth - 4, y + 70, 0xff151515);
            context.drawText(textRenderer, "TOTEM UPGRADE", x + 8, y + 8, 0xffff5555, false);
            context.drawText(textRenderer, handler.statusText(), x + 8, y + 58, 0xffdddddd, false);
            for (int slot = 0; slot < 4; slot++) {
                int slotX = x + 19 + slot * 44;
                context.fill(slotX, y + 26, slotX + 18, y + 44, 0xff555555);
            }
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            renderBackground(context, mouseX, mouseY, delta);
            super.render(context, mouseX, mouseY, delta);
            drawMouseoverTooltip(context, mouseX, mouseY);
        }
    }
}
