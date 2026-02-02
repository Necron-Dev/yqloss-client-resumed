package net.yqloss.ycr.module.system

import net.yqloss.ycr.module.system.config.ConfigEntry
import net.yqloss.ycr.state.readonlyState
import net.yqloss.ycr.state.savedState

abstract class Module(val id: String, name: String, description: String, enabled: Boolean = false) {
  val name by readonlyState("$id/name") { name }

  val description by readonlyState("$id/description") { description }

  var enabled by savedState("$id/enabled") { enabled }

  abstract val config: List<ConfigEntry>
}
