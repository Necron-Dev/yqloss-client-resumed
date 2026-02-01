package net.yqloss.ycr.state

import java.io.File
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("YCR/States")

private val STATES_FILE = File("./ycr_states.json")

object States {
  val states: MutableMap<String, JsonElement> =
      try {
        logger.info("loading states")
        Json.decodeFromString(STATES_FILE.readText(Charsets.UTF_8))
      } catch (exception: Exception) {
        logger.warn("unable to load states", exception)
        mutableMapOf()
      }

  val allStates = mutableSetOf<String>()

  private var batchWriteStackDepth = 0

  fun beginBatch() {
    batchWriteStackDepth++
    if (batchWriteStackDepth < 0) {
      throw IllegalStateException("batch stack overflow")
    }
  }

  fun endBatch() {
    batchWriteStackDepth--
    save()
    if (batchWriteStackDepth < 0) {
      throw IllegalStateException("endBatch is called more times than startBatch")
    }
  }

  inline fun batch(fn: () -> Unit) {
    beginBatch()
    try {
      fn()
    } finally {
      endBatch()
    }
  }

  fun save() {
    if (batchWriteStackDepth != 0) return
    try {
      logger.info("saving states")
      STATES_FILE.writeText(Json.encodeToString(states), Charsets.UTF_8)
    } catch (exception: Exception) {
      logger.error("unable to save states", exception)
    }
  }

  inline operator fun <reified T> invoke(id: String, defaultValue: () -> T): T {
    val jsonElement = states[id]
    return if (jsonElement != null) {
      Json.decodeFromJsonElement(jsonElement)
    } else {
      val value = defaultValue()
      states[id] = Json.encodeToJsonElement(value)
      states
      value
    }
  }

  inline operator fun <reified T> get(id: String): T? {
    val jsonElement = states[id]
    return if (jsonElement != null) {
      Json.decodeFromJsonElement(jsonElement)
    } else {
      null
    }
  }

  inline operator fun <reified T> set(id: String, value: T) {
    states[id] = Json.encodeToJsonElement(value)
    save()
  }
}
