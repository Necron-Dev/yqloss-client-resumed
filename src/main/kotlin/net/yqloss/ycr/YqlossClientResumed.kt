package net.yqloss.ycr

import com.mojang.blaze3d.systems.GpuDevice
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.yqloss.ycr.command.Commands
import net.yqloss.ycr.gui.Gui
import net.yqloss.ycr.module.Modules
import net.yqloss.ycr.state.States
import net.yqloss.ycr.util.init

val mc: Minecraft by lazy { Minecraft.getInstance() }

val device: GpuDevice
  get() = RenderSystem.getDevice()

fun initialize() {
  YqlossClientResumed.init
}

object YqlossClientResumed {
  init {
    States.batch {
      Commands.init
      Gui.init
      Modules.init
    }
  }
}
