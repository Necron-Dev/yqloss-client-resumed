package net.yqloss.ycr.event

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.yqloss.ycr.event.system.FabricEventRegistry

object ClientCommandRegistrationEvent :
    FabricEventRegistry<ClientCommandRegistrationCallback>(ClientCommandRegistrationCallback.EVENT)
