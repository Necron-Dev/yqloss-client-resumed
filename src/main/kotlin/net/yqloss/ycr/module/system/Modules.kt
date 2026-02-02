package net.yqloss.ycr.module.system

import kotlinx.serialization.Serializable
import net.yqloss.ycr.module.BetterTerminal
import net.yqloss.ycr.module.LeapMenu
import net.yqloss.ycr.state.readonlyState

object Modules {
  @Serializable
  data class ModuleInfo(
      val id: String,
      val name: String,
      val description: String,
  )

  private val modules = listOf(BetterTerminal, LeapMenu)

  init {
    readonlyState("modules/list") {
      (modules + modules + modules + modules + modules + modules + modules).map {
        ModuleInfo(it.id, it.name, it.description)
      }
    }
  }
}
