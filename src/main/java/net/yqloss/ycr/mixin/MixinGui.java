package net.yqloss.ycr.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.yqloss.ycr.callback.CallbackMixinGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
  @Inject(method = "render", at = @At("RETURN"))
  private void renderReturn(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    CallbackMixinGui.INSTANCE.renderReturn(guiGraphics, deltaTracker, ci);
  }
}
