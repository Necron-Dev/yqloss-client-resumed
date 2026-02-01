package net.yqloss.ycr.module

import net.yqloss.ycr.state.savedState

object LeapMenu : Module {
  override val id = "leap_menu"

  override val name = "Leap Menu"

  override val description = "5-grid ring-shaped leap menu."

  override var enabled by savedState("${id}/enabled") { false }
}
