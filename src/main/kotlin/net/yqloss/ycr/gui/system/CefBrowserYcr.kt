package net.yqloss.ycr.gui.system

import java.awt.Component
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.function.Consumer
import kotlin.uuid.Uuid
import net.yqloss.ycr.util.runLogging
import org.cef.CefBrowserSettings
import org.cef.CefClient
import org.cef.browser.CefBrowser
import org.cef.browser.CefBrowserYcrJava
import org.cef.browser.CefPaintEvent
import org.cef.callback.CefDragData
import org.cef.handler.CefScreenInfo
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("YCR/Browser")

class CefBrowserYcr(client: CefClient, url: String, width: Int, height: Int, framerate: Int) :
    CefBrowserYcrJava(
        client,
        url,
        CefBrowserSettings().apply { windowless_frame_rate = framerate },
    ) {
  class RenderContext(
      val width: Int,
      val height: Int,
      val buffer: ByteBuffer = ByteBuffer.allocateDirect(width * height * 4),
      val dirtyRects: ConcurrentLinkedDeque<Rectangle> = ConcurrentLinkedDeque(),
      var needFullCopy: Boolean = true,
      var needFullUpload: Boolean = false,
      var readyToRender: Boolean = false,
  )

  val uuid = Uuid.random()

  var context: RenderContext = RenderContext(width, height)
    private set

  private val component = object : Component() {}

  private fun convertButton(button: Int): Int {
    return when (button) {
      1 -> 1
      2 -> 3
      3 -> 2
      else -> 0
    }
  }

  fun moveMouse(x: Int, y: Int) {
    sendMouseEvent(
        MouseEvent(
            component,
            MouseEvent.MOUSE_MOVED,
            0,
            0,
            x,
            y,
            0,
            false,
        ))
  }

  fun pressMouse(button: Int, x: Int, y: Int) {
    sendMouseEvent(
        MouseEvent(
            component, MouseEvent.MOUSE_PRESSED, 0, 0, x, y, 1, false, convertButton(button)))
  }

  fun dragMouse(button: Int, x: Int, y: Int) {
    sendMouseEvent(
        MouseEvent(
            component, MouseEvent.MOUSE_DRAGGED, 0, 0, x, y, 1, false, convertButton(button)))
  }

  fun releaseMouse(button: Int, x: Int, y: Int) {
    sendMouseEvent(
        MouseEvent(
            component,
            MouseEvent.MOUSE_RELEASED,
            0,
            0,
            x,
            y,
            1,
            false,
            convertButton(button),
        ))
  }

  fun scrollMouse(delta: Double, x: Int, y: Int) {
    sendMouseWheelEvent(
        MouseWheelEvent(
            component,
            MouseWheelEvent.MOUSE_WHEEL,
            0,
            0,
            x,
            y,
            0,
            false,
            MouseWheelEvent.WHEEL_BLOCK_SCROLL,
            0,
            (delta * 100).toInt(),
        ))
  }

  fun resize(newWidth: Int, newHeight: Int) {
    context = RenderContext(newWidth, newHeight)
    wasResized(newWidth, newHeight)
  }

  override fun getRenderHandler() = this

  override fun getViewRect(browser: CefBrowser): Rectangle {
    val context = context
    return Rectangle(0, 0, context.width, context.height)
  }

  override fun getScreenInfo(browser: CefBrowser, screenInfo: CefScreenInfo): Boolean {
    val context = context
    screenInfo.Set(
        0.0,
        32,
        8,
        false,
        Rectangle(0, 0, context.width, context.height),
        Rectangle(0, 0, context.width, context.height),
    )
    return true
  }

  override fun getScreenPoint(browser: CefBrowser, viewPoint: Point): Point {
    return Point(viewPoint)
  }

  override fun onPaint(
      browser: CefBrowser,
      popup: Boolean,
      dirtyRects: Array<Rectangle>,
      frame: ByteBuffer,
      width: Int,
      height: Int,
  ) =
      runLogging(logger, "onPaint") {
        if (popup) return@runLogging
        val context = context
        if (width != context.width || height != context.height) {
          return@runLogging
        }
        if (context.needFullCopy) {
          context.needFullCopy = false
          context.buffer.put(0, frame, 0, context.buffer.capacity())
          context.dirtyRects.clear()
          context.needFullUpload = true
        } else {
          val lineBytes = width shl 2
          for (rect in dirtyRects) {
            var ptr = (rect.x + rect.y * width) shl 2
            var y = 0
            while (y < rect.height) {
              context.buffer.put(ptr, frame, ptr, rect.width shl 2)
              ptr += lineBytes
              ++y
            }
          }
          context.dirtyRects += dirtyRects
        }
      }

  override fun onCursorChange(browser: CefBrowser, cursorType: Int): Boolean {
    return true
  }

  override fun startDragging(
      browser: CefBrowser?,
      dragData: CefDragData?,
      mask: Int,
      x: Int,
      y: Int,
  ) = true

  override fun updateDragCursor(browser: CefBrowser?, operation: Int) {}

  override fun onPopupShow(browser: CefBrowser?, show: Boolean) {}

  override fun onPopupSize(browser: CefBrowser?, size: Rectangle?) {}

  override fun addOnPaintListener(listener: Consumer<CefPaintEvent?>?) {}

  override fun setOnPaintListener(listener: Consumer<CefPaintEvent?>?) {}

  override fun removeOnPaintListener(listener: Consumer<CefPaintEvent?>?) {}

  override fun getUIComponent() = component

  override fun createScreenshot(nativeResolution: Boolean): CompletableFuture<BufferedImage> {
    throw NotImplementedError()
  }

  override fun executeJavaScript(code: String, url: String, line: Int) {
    super.executeJavaScript(code, url, line)
  }
}
