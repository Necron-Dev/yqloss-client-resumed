package net.yqloss.ycr.gui.system

import com.mojang.blaze3d.opengl.GlStateManager
import com.mojang.blaze3d.opengl.GlTexture
import com.mojang.blaze3d.textures.GpuTexture
import com.mojang.blaze3d.textures.TextureFormat
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.resources.ResourceLocation
import net.yqloss.ycr.device
import net.yqloss.ycr.event.FrameEvent
import net.yqloss.ycr.mc
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12

class GuiLayer(private val browser: CefBrowserYcr) : AutoCloseable {
  val uuid = browser.uuid

  private val location = ResourceLocation.tryParse("ycr:gui/layer_${Uuid.random()}")!!

  private val texture =
      device.createTexture(
          location.toString(),
          GpuTexture.USAGE_TEXTURE_BINDING,
          TextureFormat.RGBA8,
          1,
          1,
          1,
          1,
      ) as GlTexture

  private val textureView = device.createTextureView(texture)

  private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

  private val randomToken
    get() = Uuid.random().toString()

  private var token = randomToken

  init {
    mc.textureManager.register(
        location,
        object : AbstractTexture() {
          init {
            texture = this@GuiLayer.texture
            textureView = this@GuiLayer.textureView
          }
        },
    )

    FrameEvent { commit() }
  }

  fun commit() {
    val width = mc.window.width
    val height = mc.window.height
    val context = browser.context
    if (width != context.width || height != context.height) {
      browser.resize(width, height)
      return
    }
    GlStateManager._bindTexture(texture.glId())
    GlStateManager._pixelStore(GL11.GL_UNPACK_ROW_LENGTH, width)
    if (context.needFullUpload) {
      context.needFullUpload = false
      GlStateManager._pixelStore(GL11.GL_UNPACK_SKIP_PIXELS, 0)
      GlStateManager._pixelStore(GL11.GL_UNPACK_SKIP_ROWS, 0)
      GL11.glTexImage2D(
          GL11.GL_TEXTURE_2D,
          0,
          GL11.GL_RGBA8,
          width,
          height,
          0,
          GL12.GL_BGRA,
          GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
          context.buffer,
      )
      context.readyToRender = true
    } else if (context.readyToRender) {
      while (true) {
        val rect = context.dirtyRects.poll() ?: break
        GlStateManager._pixelStore(GL11.GL_UNPACK_SKIP_PIXELS, rect.x)
        GlStateManager._pixelStore(GL11.GL_UNPACK_SKIP_ROWS, rect.y)
        GL11.glTexSubImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            rect.x,
            rect.y,
            rect.width,
            rect.height,
            GL12.GL_BGRA,
            GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
            context.buffer,
        )
      }
    }
  }

  fun render(guiGraphics: GuiGraphics) {
    val width = mc.window.width
    val height = mc.window.height
    val context = browser.context
    if (width != context.width || height != context.height) return
    if (!context.readyToRender || token != Gui.token[this]) return
    guiGraphics.pose().pushMatrix()
    val scale = 1.0F / mc.window.guiScale
    guiGraphics.pose().scale(scale, scale)
    guiGraphics.blit(location, 0, 0, width, height, 0.0F, 1.0F, 0.0F, 1.0F)
    guiGraphics.pose().popMatrix()
  }

  fun execute(script: String) {
    browser.executeJavaScript(script, browser.url, 0)
  }

  fun launchedExecute(script: String) {
    coroutineScope.launch { execute(script) }
  }

  fun open(url: String) {
    val newToken = randomToken
    token = newToken
    Gui.href[this] = "$url?token=$newToken"
  }

  fun moveMouse(x: Double, y: Double) {
    browser.moveMouse(x.toInt(), y.toInt())
  }

  fun pressMouse(button: Int, x: Double, y: Double) {
    browser.pressMouse(button, x.toInt(), y.toInt())
  }

  fun dragMouse(button: Int, x: Double, y: Double) {
    browser.dragMouse(button, x.toInt(), y.toInt())
  }

  fun releaseMouse(button: Int, x: Double, y: Double) {
    browser.releaseMouse(button, x.toInt(), y.toInt())
  }

  fun scrollMouse(delta: Double, x: Double, y: Double) {
    browser.scrollMouse(delta, x.toInt(), y.toInt())
  }

  override fun close() {
    texture.close()
    textureView.close()
    mc.textureManager.release(location)
    browser.close(true)
  }
}
