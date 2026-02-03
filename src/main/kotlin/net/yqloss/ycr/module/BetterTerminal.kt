package net.yqloss.ycr.module

import net.yqloss.ycr.module.system.Module
import net.yqloss.ycr.module.system.config.config
import net.yqloss.ycr.module.system.config.style.bool.switch

object BetterTerminal :
    Module("better-terminal", "Better Terminal", "Better floor-7 terminal GUI and queue-terms.") {
  override val config = config {
    repeat(100) {
      ::enabled {
        name = "Enabled"
        style = switch
      }
    }
  }
}
