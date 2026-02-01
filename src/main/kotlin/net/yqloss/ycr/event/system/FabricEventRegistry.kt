package net.yqloss.ycr.event.system

import net.fabricmc.fabric.api.event.Event

open class FabricEventRegistry<T>(val event: Event<T>) : EventRegistry<T> {
  override fun invoke(handler: T) {
    event.register(handler)
  }
}
