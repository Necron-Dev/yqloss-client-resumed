package net.yqloss.ycr.gui.system

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.yqloss.ycr.event.GuiRenderEvent
import net.yqloss.ycr.mc

open class BrowserScreen(title: String, private val layer: GuiLayer) :
    Screen(Component.literal(title)) {
  protected fun execute(script: String) = layer.execute(script)

  protected fun open(url: String) = layer.open(url)

  override fun added() {
    layer.markDirty()
  }

  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    GuiRenderEvent.fire(GuiRenderEvent(GuiRenderEvent.Layer.BROWSER_SCREEN, guiGraphics))
  }

  override fun removed() {
    open(Gui.EMPTY_PAGE)
    super.removed()
  }

  override fun mouseMoved(mouseX: Double, mouseY: Double) {
    layer.moveMouse(mouseX.scaled, mouseY.scaled)
    super.mouseMoved(mouseX, mouseY)
  }

  override fun mouseClicked(event: MouseButtonEvent, isDoubleClick: Boolean): Boolean {
    layer.pressMouse(event.button(), event.x.scaled, event.y.scaled)
    return super.mouseClicked(event, isDoubleClick)
  }

  override fun mouseReleased(event: MouseButtonEvent): Boolean {
    layer.releaseMouse(event.button(), event.x.scaled, event.y.scaled)
    return super.mouseReleased(event)
  }

  override fun mouseScrolled(
      mouseX: Double,
      mouseY: Double,
      scrollX: Double,
      scrollY: Double,
  ): Boolean {
    layer.scrollMouse(scrollY, mouseX.scaled, mouseY.scaled)
    return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)
  }

  val Double.scaled
    get() = this * mc.window.guiScale
}
