package net.yqloss.ycr.state

import kotlin.reflect.KProperty
import kotlinx.serialization.json.Json
import net.yqloss.ycr.event.BrowserEvent
import net.yqloss.ycr.gui.system.Gui
import net.yqloss.ycr.gui.system.get
import net.yqloss.ycr.gui.system.postJson
import net.yqloss.ycr.gui.system.respond
import net.yqloss.ycr.gui.system.respondJson
import net.yqloss.ycr.state.system.ReadWriteState
import net.yqloss.ycr.state.system.States

inline fun <reified T> state(id: String, defaultValue: () -> T): ReadWriteState<T> {
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

  return object : ReadWriteState<T> {
    override val id = id

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = savedValue

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
      savedValue = value
      val json = Json.encodeToString(value)
      Gui.launchedExecuteInAllLayers("window['set_$id'](null, $json)")
    }
  }
}
