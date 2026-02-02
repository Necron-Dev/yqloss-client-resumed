package net.yqloss.ycr.gui.system

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.yqloss.ycr.mc

object ScreenProxy {
  var proxy: Screen? = null
    set(value) {
      proxy?.removed()
      field = value
      value?.init(mc, mc.window.guiScaledWidth, mc.window.guiScaledHeight)
      value?.added()
    }

  fun mouseClicked(
      screen: Screen,
      mouseButtonEvent: MouseButtonEvent,
      isDoubleClick: Boolean,
  ): Boolean {
    return (proxy ?: screen).mouseClicked(mouseButtonEvent, isDoubleClick)
  }

  fun mouseReleased(screen: Screen, mouseButtonEvent: MouseButtonEvent): Boolean {
    return (proxy ?: screen).mouseReleased(mouseButtonEvent)
  }

  fun renderWithTooltipAndSubtitles(
      screen: Screen,
      guiGraphics: GuiGraphics,
      mouseX: Int,
      mouseY: Int,
      partialTicks: Float,
  ) {
    return (proxy ?: screen).renderWithTooltipAndSubtitles(
        guiGraphics,
        mouseX,
        mouseY,
        partialTicks,
    )
  }
}
