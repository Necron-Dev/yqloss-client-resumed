package net.yqloss.ycr.event

import net.minecraft.client.multiplayer.ClientLevel
import net.yqloss.ycr.event.system.SimpleEventRegistry

data class WorldLoadEvent(val level: ClientLevel?) {
  companion object : SimpleEventRegistry<WorldLoadEvent>()
}
