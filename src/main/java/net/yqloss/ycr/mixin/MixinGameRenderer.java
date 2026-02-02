package net.yqloss.ycr.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.yqloss.ycr.gui.system.ScreenProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
  @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
  private void renderRedirectRenderWithTooltipAndSubtitles(Screen instance, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    ScreenProxy.INSTANCE.renderWithTooltipAndSubtitles(instance, guiGraphics, mouseX, mouseY, partialTick);
  }
}
