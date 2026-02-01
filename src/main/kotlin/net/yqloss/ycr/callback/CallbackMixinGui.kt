package net.yqloss.ycr.callback

import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.yqloss.ycr.event.GuiRenderEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object CallbackMixinGui {
  fun renderReturn(
      guiGraphics: GuiGraphics,
      deltaTracker: DeltaTracker,
      callbackInfo: CallbackInfo,
  ) {
    GuiRenderEvent.fire(GuiRenderEvent(GuiRenderEvent.Layer.HUD, guiGraphics))
  }
}
