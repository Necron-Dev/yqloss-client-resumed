package net.yqloss.ycr.state

import net.yqloss.ycr.event.BrowserEvent
import net.yqloss.ycr.gui.system.get
import net.yqloss.ycr.gui.system.respondJson
import net.yqloss.ycr.state.system.ReadableState
import net.yqloss.ycr.state.system.States

inline fun <reified T> readonlyState(id: String, value: () -> T): ReadableState<T> {
  States.allStates += id

  val savedValue = value()

  BrowserEvent { get("state/get/$id") { respondJson(savedValue) } }

  return object : ReadableState<T> {
    override val id = id

    override fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): T = savedValue
  }
}
