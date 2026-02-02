package net.yqloss.ycr.gui.system

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.yqloss.ycr.mc
import org.lwjgl.glfw.GLFW

object ScreenProxy {
  var proxy: Screen? = null
    set(value) {
      proxy?.removed()
      field = value
      value?.init(mc, mc.window.guiScaledWidth, mc.window.guiScaledHeight)
      value?.added()
    }

  fun resize(screen: Screen, minecraft: Minecraft, width: Int, height: Int) {
    screen.resize(minecraft, width, height)
    proxy?.resize(minecraft, width, height)
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

  fun afterMouseAction(screen: Screen) {
    (proxy ?: screen).afterMouseAction()
  }

  fun mouseScrolled(
      screen: Screen,
      mouseX: Double,
      mouseY: Double,
      scrollX: Double,
      scrollY: Double,
  ): Boolean {
    return (proxy ?: screen).mouseScrolled(mouseX, mouseY, scrollX, scrollY)
  }

  fun mouseMoved(
      screen: Screen,
      mouseX: Double,
      mouseY: Double,
  ) {
    (proxy ?: screen).mouseMoved(mouseX, mouseY)
  }

  fun afterMouseMove(screen: Screen) {
    (proxy ?: screen).afterMouseMove()
  }

  fun mouseDragged(
      screen: Screen,
      mouseButtonEvent: MouseButtonEvent,
      mouseX: Double,
      mouseY: Double,
  ): Boolean {
    return (proxy ?: screen).mouseDragged(mouseButtonEvent, mouseX, mouseY)
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

  fun charTyped(screen: Screen, characterEvent: CharacterEvent): Boolean {
    return (proxy ?: screen).charTyped(characterEvent)
  }

  fun keyPressed(screen: Screen, keyEvent: KeyEvent): Boolean {
    if (keyEvent.key == GLFW.GLFW_KEY_ESCAPE) return screen.keyPressed(keyEvent)
    return (proxy ?: screen).keyPressed(keyEvent)
  }

  fun keyReleased(screen: Screen, keyEvent: KeyEvent): Boolean {
    if (keyEvent.key == GLFW.GLFW_KEY_ESCAPE) return screen.keyReleased(keyEvent)
    return (proxy ?: screen).keyReleased(keyEvent)
  }

  fun afterKeyboardAction(screen: Screen) {
    (proxy ?: screen).afterKeyboardAction()
  }

  fun getFocused(screen: Screen): GuiEventListener? {
    return (proxy ?: screen).focused
  }
}
