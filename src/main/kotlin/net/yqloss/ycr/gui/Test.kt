package net.yqloss.ycr.gui

import net.yqloss.ycr.gui.system.BrowserScreen
import net.yqloss.ycr.gui.system.Gui
import net.yqloss.ycr.gui.system.GuiLayer

class Test(guiLayer: GuiLayer) : BrowserScreen("Test", guiLayer) {
  override fun added() {
    open("${Gui.HOST}/index.html#screen/config")
  }
}
