package net.yqloss.ycr.event

import kotlin.uuid.Uuid
import net.yqloss.ycr.event.system.SimpleEventRegistry

data class BrowserEvent(
    val uuid: Uuid,
    val method: String,
    val url: String,
    val payload: ByteArray,
    var mutMimeType: String = "text/plain",
    var mutStatus: Int = 404,
    var mutResponse: ByteArray = byteArrayOf(),
    var mutDone: Boolean = false,
) {
  companion object : SimpleEventRegistry<BrowserEvent>()
}
