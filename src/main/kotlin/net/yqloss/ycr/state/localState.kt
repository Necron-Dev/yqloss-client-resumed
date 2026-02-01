package net.yqloss.ycr.state

import kotlin.uuid.Uuid
import kotlinx.serialization.json.Json
import net.yqloss.ycr.event.BrowserEvent
import net.yqloss.ycr.gui.*

interface LocalState<T> {
  operator fun get(layer: GuiLayer): T

  operator fun set(layer: GuiLayer, value: T)
}

inline fun <reified T> localState(
    id: String,
    crossinline defaultValue: () -> T,
): LocalState<T> {
  States.allStates += id

  val savedValues = mutableMapOf<Uuid, T>()

  BrowserEvent {
    get("state/get/$id") {
      respondJson(synchronized(savedValues) { savedValues.getOrPut(uuid, defaultValue) })
    }

    postJson<T>("state/set/$id") {
      synchronized(savedValues) { savedValues[uuid] = it }
      respond(200)
    }
  }

  return object : LocalState<T> {
    override fun get(layer: GuiLayer) =
        synchronized(savedValues) { savedValues.getOrPut(layer.uuid, defaultValue) }

    override fun set(layer: GuiLayer, value: T) {
      synchronized(savedValues) {
        savedValues[layer.uuid] = value
        val json = Json.encodeToString(value)
        layer.launchedExecute("window['set_$id'](null,$json)")
      }
    }
  }
}
