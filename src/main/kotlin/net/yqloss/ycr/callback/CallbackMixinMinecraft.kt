package net.yqloss.ycr.callback

import net.minecraft.client.gui.screens.Screen
import net.yqloss.ycr.event.FrameEvent
import net.yqloss.ycr.event.ScreenProxyEvent
import net.yqloss.ycr.event.TickEvent
import net.yqloss.ycr.gui.system.ScreenProxy
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object CallbackMixinMinecraft {
  fun tickHead(callbackInfo: CallbackInfo) {
    TickEvent.fire(TickEvent)
  }

  fun runTickHead(callbackInfo: CallbackInfo) {
    FrameEvent.fire(TickEvent)
  }

  fun setScreenReturn(screen: Screen?, callbackInfo: CallbackInfo) {
    if (screen == null) {
      ScreenProxy.proxy = null
      return
    }
    ScreenProxy.proxy = ScreenProxyEvent(screen).also(ScreenProxyEvent::fire).mutProxy
  }
}
