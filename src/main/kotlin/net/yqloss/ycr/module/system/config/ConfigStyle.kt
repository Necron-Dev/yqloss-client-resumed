package net.yqloss.ycr.module.system.config

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

interface ConfigStyle<T> {
  val id: String

  val data: JsonElement?
}

inline fun <V, reified T> style(id: String, crossinline dataFn: () -> T? = { null }) =
    object : ConfigStyle<V> {
      override val id = id

      override val data = Json.encodeToJsonElement(dataFn())
    }
