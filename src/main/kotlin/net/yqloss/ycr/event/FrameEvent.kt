package net.yqloss.ycr.event

import net.yqloss.ycr.event.system.SimpleEventRegistry

data object FrameEvent : SimpleEventRegistry<TickEvent>()
