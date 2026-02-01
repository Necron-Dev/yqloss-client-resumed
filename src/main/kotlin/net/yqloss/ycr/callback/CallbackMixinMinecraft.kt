package net.yqloss.ycr.callback

import net.yqloss.ycr.event.TickEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object CallbackMixinMinecraft {
  fun runTickHead(renderLevel: Boolean, callbackInfo: CallbackInfo) {
    TickEvent.fire(TickEvent)
  }
}
