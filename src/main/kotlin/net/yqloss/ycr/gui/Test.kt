package net.yqloss.ycr.gui

class Test(guiLayer: GuiLayer) : BrowserScreen(guiLayer) {
  init {
    open("${Gui.HOST}/index.html#screen/config")
  }
}
