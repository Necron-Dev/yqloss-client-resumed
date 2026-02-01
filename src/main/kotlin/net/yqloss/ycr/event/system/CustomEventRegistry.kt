package net.yqloss.ycr.event.system

open class CustomEventRegistry<T>(private val registerMethod: (T) -> Unit) : EventRegistry<T> {
  override fun invoke(handler: T) {
    registerMethod(handler)
  }
}
