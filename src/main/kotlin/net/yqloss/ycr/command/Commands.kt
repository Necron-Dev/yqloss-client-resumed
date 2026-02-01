package net.yqloss.ycr.command

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.yqloss.ycr.event.ClientCommandRegistrationEvent

object Commands {
  init {
    ClientCommandRegistrationEvent { dispatcher, context ->
      dispatcher.register(ClientCommandManager.literal("ycr").executes { it.ycr() })
    }
  }
}
