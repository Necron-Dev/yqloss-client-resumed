package net.yqloss.ycr.gui.system

import me.friwi.jcefmaven.CefAppBuilder
import net.yqloss.ycr.event.BrowserEvent
import net.yqloss.ycr.event.GuiRenderEvent
import net.yqloss.ycr.mc
import net.yqloss.ycr.state.localState
import net.yqloss.ycr.state.savedState
import net.yqloss.ycr.state.system.States
import net.yqloss.ycr.state.webState
import net.yqloss.ycr.util.getMimeType
import net.yqloss.ycr.util.uses
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.callback.CefCallback
import org.cef.handler.*
import org.cef.misc.BoolRef
import org.cef.misc.IntRef
import org.cef.misc.StringRef
import org.cef.network.CefPostDataElement
import org.cef.network.CefRequest
import org.cef.network.CefResponse
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.math.min
import kotlin.uuid.Uuid

private val logger = LoggerFactory.getLogger("YCR/Gui")

private class YcrResourceHandlerAdapter(private val uuid: Uuid) : CefResourceHandlerAdapter() {
  lateinit var event: BrowserEvent
  var offset = 0

  override fun processRequest(request: CefRequest, callback: CefCallback): Boolean {
    val vector = Vector<CefPostDataElement>()
    request.postData?.getElements(vector)
    val payloadSegments =
        vector
            .mapNotNull {
              when (it.type) {
                CefPostDataElement.Type.PDE_TYPE_BYTES -> it
                else -> null
              }
            }
            .map {
              val bytes = ByteArray(it.bytesCount)
              it.getBytes(it.bytesCount, bytes)
              bytes
            }
    val payload = ByteArray(payloadSegments.sumOf { it.size })
    var currentOffset = 0
    for (segment in payloadSegments) {
      segment.copyInto(payload, currentOffset)
      currentOffset += segment.size
    }
    val event =
        BrowserEvent(uuid, request.method, request.url.removePrefix("http://-ycr-"), payload)
    BrowserEvent.fire(event)
    this.event = event
    offset = 0
    callback.Continue()
    return true
  }

  override fun getResponseHeaders(
      response: CefResponse,
      responseLength: IntRef,
      redirectUrl: StringRef,
  ) {
    val event = event
    response.mimeType = event.mutMimeType
    response.status = event.mutStatus
    response.setHeaderByName("Access-Control-Allow-Origin", "*", true)
    responseLength.set(event.mutResponse.size)
  }

  override fun readResponse(
      dataOut: ByteArray,
      bytesToRead: Int,
      bytesRead: IntRef,
      callback: CefCallback,
  ): Boolean {
    val event = event
    val offset = offset
    if (offset >= event.mutResponse.size) return false
    val bytesToRead = min(bytesToRead, event.mutResponse.size - offset)
    event.mutResponse.copyInto(dataOut, 0, offset, offset + bytesToRead)
    this.offset = offset + bytesToRead
    bytesRead.set(bytesToRead)
    callback.Continue()
    return true
  }
}

private object YcrResourceRequestHandler : CefResourceRequestHandlerAdapter() {
  override fun getResourceHandler(
      browser: CefBrowser?,
      frame: CefFrame?,
      request: CefRequest,
  ): CefResourceHandler? {
    if (request.url.startsWith("http://-ycr-")) {
      return YcrResourceHandlerAdapter((browser as CefBrowserYcr).uuid)
    }
    return null
  }
}

private object YcrRequestHandler : CefRequestHandlerAdapter() {
  override fun getResourceRequestHandler(
      browser: CefBrowser?,
      frame: CefFrame?,
      request: CefRequest,
      isNavigation: Boolean,
      isDownload: Boolean,
      requestInitiator: String?,
      disableDefaultHandling: BoolRef?,
  ): CefResourceRequestHandler? {
    if (request.url.startsWith("http://-ycr-")) {
      return YcrResourceRequestHandler
    }
    return null
  }
}

object Gui {
  val HOST = if (System.getenv("YCRDEBUG") == "true") "http://localhost:5173" else "http://-ycr-web"

  val EMPTY_PAGE = "$HOST/index.html"

  val scale by savedState("gui/scale") { 1.0 }

  val framerate by savedState("gui/framerate") { 60 }

  val href = localState("href") { EMPTY_PAGE }

  val token = localState("token") { "" }

  private val app =
      with(CefAppBuilder()) {
        cefSettings.windowless_rendering_enabled = true
        cefSettings.background_color = cefSettings.ColorType(0, 0, 0, 0)
        // --use-gl=
        addJcefArgs("--remote-debugging-port=9222")
        addJcefArgs("--allow-file-access-from-files")
        addJcefArgs("--disable-web-security")
        addJcefArgs("--no-sandbox")
        build()
      }

  private val client by lazy { app.createClient().apply { addRequestHandler(YcrRequestHandler) } }

  private val layers = mutableListOf<GuiLayer>()

  private fun createLayer(): GuiLayer {
    val browser = CefBrowserYcr(client, EMPTY_PAGE, mc.window.width, mc.window.height, framerate)
    val layer = GuiLayer(browser)
    logger.info("created browser ${browser.uuid}")
    browser.createImmediately()
    val isPendingField = browser.isPendingField
    while (!isPendingField.getBoolean(browser)) {
      Thread.yield()
    }
    layers += layer
    return layer
  }

  val hudLayer by lazy { createLayer().apply { open("$HOST/index.html#/hud") } }

  val screenLayer by lazy { createLayer() }

  fun executeInAllLayers(script: String) {
    for (layer in layers) {
      layer.execute(script)
    }
  }

  fun launchedExecuteInAllLayers(script: String) {
    for (layer in layers) {
      layer.launchedExecute(script)
    }
  }

  init {
    webState("uuid") { uuid }

    webState("preload") { States.allStates }

    GuiRenderEvent {
      when (layer) {
        //        GuiRenderEvent.Layer.HUD -> hudLayer.render(guiGraphics)
        GuiRenderEvent.Layer.BROWSER_SCREEN -> screenLayer.render(guiGraphics)
        else -> {}
      }
    }

    BrowserEvent {
      get("web/") {
        uses {
          val filePath =
              url.removePrefix("web/")
                  .run { if ('#' in this) substring(0, indexOf('#')) else this }
                  .run { if ('?' in this) substring(0, indexOf('?')) else this }

          when (val stream = javaClass.getResourceAsStream("/web/$filePath")) {
            null -> {
              respond(404)
            }

            else -> {
              use(stream)
              respond(stream.readAllBytes(), mimeType = getMimeType(filePath))
            }
          }
        }
      }
    }
  }
}
