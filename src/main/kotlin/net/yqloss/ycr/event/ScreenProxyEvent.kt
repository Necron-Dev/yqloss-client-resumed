package net.yqloss.ycr.event

import net.minecraft.client.gui.screens.Screen
import net.yqloss.ycr.event.system.SimpleEventRegistry

data class ScreenProxyEvent(val screen: Screen, var mutProxy: Screen? = null) {
  companion object : SimpleEventRegistry<ScreenProxyEvent>()
}
