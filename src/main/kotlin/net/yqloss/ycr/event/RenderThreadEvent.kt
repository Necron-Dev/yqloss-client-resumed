package net.yqloss.ycr.event

import com.mojang.blaze3d.systems.RenderSystem
import net.yqloss.ycr.event.system.CustomEventRegistry

object RenderThreadEvent :
    CustomEventRegistry<() -> Unit>({ handler -> RenderSystem.queueFencedTask { handler() } })
