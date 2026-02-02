package net.yqloss.ycr.module.system.config

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.jvm.isAccessible
import net.yqloss.ycr.module.system.Module
import net.yqloss.ycr.state.system.State
import net.yqloss.ycr.state.webState

class ConfigBuilderContext(private val entries: MutableList<ConfigEntry>) {
  data class EntrySetupContext<T>(var id: String? = null) {
    var name: String? = null
    var description: String? = null
    lateinit var style: ConfigStyle<T>
  }

  operator fun <T> KMutableProperty0<T>.invoke(setup: EntrySetupContext<T>.() -> Unit) {
    isAccessible = true
    val id = (getDelegate() as State).id
    val context = EntrySetupContext<T>(id)
    context.setup()
    entries +=
        ConfigEntry(
            id = context.id,
            name = context.name,
            description = context.description,
            style = context.style.id,
            extra = context.style.data,
        )
  }
}

inline fun Module.config(fn: ConfigBuilderContext.() -> Unit): List<ConfigEntry> {
  val config = buildList { ConfigBuilderContext(this).apply(fn) }
  webState("$id/config") { config }
  return config
}
