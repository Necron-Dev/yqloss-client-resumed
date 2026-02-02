package net.yqloss.ycr.module

import net.minecraft.client.gui.screens.inventory.ContainerScreen
import net.yqloss.ycr.event.ScreenProxyEvent
import net.yqloss.ycr.gui.Test
import net.yqloss.ycr.gui.system.Gui
import net.yqloss.ycr.module.system.Module
import net.yqloss.ycr.module.system.config.config
import net.yqloss.ycr.module.system.config.style.bool.switch

object LeapMenu : Module("leap_menu", "Leap Menu", "5-grid ring-shaped leap menu.") {
  init {
    ScreenProxyEvent {
      if (screen is ContainerScreen) {
        mutProxy = Test(Gui.screenLayer)
      }
    }
  }

  override val config = config {
    ::enabled {
      name = "Enabled"
      style = switch
    }
  }
}
