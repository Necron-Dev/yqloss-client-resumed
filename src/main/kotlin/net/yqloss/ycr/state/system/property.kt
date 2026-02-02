package net.yqloss.ycr.state.system

import kotlin.reflect.KProperty

interface State {
  val id: String
}

interface ReadableState<T> : State {
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T
}

interface ReadWriteState<T> : ReadableState<T> {
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}
