package net.yqloss.ycr.state

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlinx.serialization.json.Json
import net.yqloss.ycr.event.BrowserEvent
import net.yqloss.ycr.gui.*

inline fun <reified T> state(id: String, defaultValue: () -> T): ReadWriteProperty<Any?, T> {
  States.allStates += id

  var savedValue = defaultValue()

  BrowserEvent {
    get("state/get/$id") { respondJson(savedValue) }

    postJson<T>("state/set/$id") {
      savedValue = it
      val json = Json.encodeToString(it)
      Gui.launchedExecuteInAllLayers("window['set_$id']('$uuid',$json)")
      respond(200)
    }
  }

  return object : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = savedValue

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
      savedValue = value
      val json = Json.encodeToString(value)
      Gui.launchedExecuteInAllLayers("window['set_$id'](null,$json)")
    }
  }
}
