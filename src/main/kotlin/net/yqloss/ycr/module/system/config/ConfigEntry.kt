package net.yqloss.ycr.module.system.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ConfigEntry(
    private val id: String?,
    private val name: String?,
    private val description: String?,
    private val style: String,
    private val extra: JsonElement?,
)
