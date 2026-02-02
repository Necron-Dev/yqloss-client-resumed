package net.yqloss.ycr.module.system.config.style.bool

import net.yqloss.ycr.module.system.config.ConfigBuilderContext
import net.yqloss.ycr.module.system.config.style

val ConfigBuilderContext.switch
  get() = style<Boolean, Unit>("switch")
