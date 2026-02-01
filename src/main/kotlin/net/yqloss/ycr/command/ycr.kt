package net.yqloss.ycr.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.yqloss.ycr.gui.Gui
import net.yqloss.ycr.gui.Test

fun CommandContext<FabricClientCommandSource>.ycr(): Int {
  Gui.display(::Test)
  return Command.SINGLE_SUCCESS
}
