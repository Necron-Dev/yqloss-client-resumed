package net.yqloss.ycr.state

import kotlin.properties.ReadOnlyProperty
import net.yqloss.ycr.event.BrowserEvent
import net.yqloss.ycr.gui.get
import net.yqloss.ycr.gui.respondJson

inline fun <reified T> readonlyState(id: String, value: () -> T): ReadOnlyProperty<Any?, T> {
  States.allStates += id

  val savedValue = value()

  BrowserEvent { get("state/get/$id") { respondJson(savedValue) } }

  return ReadOnlyProperty { _, _ -> savedValue }
}
