package net.yqloss.ycr.event

import net.yqloss.ycr.event.system.CustomEventRegistry
import net.yqloss.ycr.mc

object ScheduleEvent : CustomEventRegistry<() -> Unit>({ mc.schedule { it() } })
