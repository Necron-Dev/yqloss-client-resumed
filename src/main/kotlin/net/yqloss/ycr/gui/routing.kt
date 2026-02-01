package net.yqloss.ycr.gui

import kotlinx.serialization.json.Json
import net.yqloss.ycr.event.BrowserEvent

inline fun BrowserEvent.request(endpoint: String, method: String? = null, handler: () -> Unit) {
  if (mutDone) return
  if (method != null && method != this.method) return
  if (!url.startsWith(endpoint)) return
  handler()
}

inline fun <reified T> BrowserEvent.requestJson(
    endpoint: String,
    method: String? = null,
    handler: (T) -> Unit,
) {
  request(endpoint, method) { handler(Json.decodeFromString(payload.toString(Charsets.UTF_8))) }
}

inline fun BrowserEvent.get(endpoint: String, handler: () -> Unit) {
  request(endpoint, "GET", handler)
}

inline fun <reified T> BrowserEvent.getJson(endpoint: String, handler: (T) -> Unit) {
  requestJson(endpoint, "GET", handler)
}

inline fun BrowserEvent.post(endpoint: String, handler: () -> Unit) {
  request(endpoint, "POST", handler)
}

inline fun <reified T> BrowserEvent.postJson(endpoint: String, handler: (T) -> Unit) {
  requestJson(endpoint, "POST", handler)
}

fun BrowserEvent.respond(
    body: ByteArray,
    statusCode: Int = 200,
    mimeType: String = "text/plain",
) {
  mutDone = true
  mutStatus = statusCode
  mutMimeType = mimeType
  mutResponse = body
}

fun BrowserEvent.respond(body: String, statusCode: Int = 200, mimeType: String = "text/plain") {
  respond(body.toByteArray(Charsets.UTF_8), statusCode, mimeType)
}

fun BrowserEvent.respond(statusCode: Int = 200) {
  respond(ByteArray(0), statusCode)
}

inline fun <reified T> BrowserEvent.respondJson(data: T, statusCode: Int = 200) {
  respond(
      Json.encodeToString(data).toByteArray(Charsets.UTF_8),
      statusCode,
      "application/json",
  )
}
