package net.yqloss.ycr.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.yqloss.ycr.event.ScheduleEvent
import net.yqloss.ycr.gui.Test
import net.yqloss.ycr.gui.system.Gui
import net.yqloss.ycr.mc

fun CommandContext<FabricClientCommandSource>.ycr(): Int {
  ScheduleEvent { mc.setScreen(Test(Gui.screenLayer)) }
  return Command.SINGLE_SUCCESS
}
