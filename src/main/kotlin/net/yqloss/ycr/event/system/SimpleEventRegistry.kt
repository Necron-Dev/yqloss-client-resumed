package net.yqloss.ycr.event.system

abstract class SimpleEventRegistry<T> : EventRegistry<T.() -> Unit> {
  private val listeners = mutableListOf<T.() -> Unit>()

  override operator fun invoke(handler: T.() -> Unit) {
    listeners += handler
  }

  fun fire(event: T) {
    for (listener in listeners) {
      event.listener()
    }
  }
}
