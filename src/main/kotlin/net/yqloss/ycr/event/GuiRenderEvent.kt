package net.yqloss.ycr.event

import net.minecraft.client.gui.GuiGraphics
import net.yqloss.ycr.event.system.SimpleEventRegistry

data class GuiRenderEvent(val layer: Layer, val guiGraphics: GuiGraphics) {
  enum class Layer {
    HUD,
    BROWSER_SCREEN,
  }

  companion object : SimpleEventRegistry<GuiRenderEvent>()
}
