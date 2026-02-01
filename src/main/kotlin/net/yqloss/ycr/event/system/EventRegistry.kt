package net.yqloss.ycr.event.system

interface EventRegistry<T> {
  operator fun invoke(handler: T)
}
