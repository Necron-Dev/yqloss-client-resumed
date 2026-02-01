package net.yqloss.ycr.module

import net.yqloss.ycr.state.savedState

object BetterTerminal : Module {
  override val id = "better_terminal"

  override val name = "Better Terminal"

  override val description = "Better floor-7 terminal GUI and queue-terms."

  override var enabled by savedState("$id/enabled") { false }
}
