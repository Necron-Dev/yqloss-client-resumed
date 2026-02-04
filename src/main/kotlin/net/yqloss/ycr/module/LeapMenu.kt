package net.yqloss.ycr.module

import kotlinx.serialization.Serializable
import net.minecraft.client.gui.screens.inventory.ContainerScreen
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.TooltipFlag
import net.yqloss.ycr.event.*
import net.yqloss.ycr.gui.system.BrowserScreen
import net.yqloss.ycr.gui.system.Gui
import net.yqloss.ycr.gui.system.postJson
import net.yqloss.ycr.gui.system.respond
import net.yqloss.ycr.mc
import net.yqloss.ycr.module.system.Module
import net.yqloss.ycr.module.system.config.config
import net.yqloss.ycr.module.system.config.style.bool.switch
import net.yqloss.ycr.state.state
import net.yqloss.ycr.util.middleClickContainer

private val REGEX_IN_BRACKETS = Regex("\\[.*]")
private val REGEX_TAB_NAME =
    Regex("([A-Za-z0-9_]{1,16})\\s*\\((Archer|Berserk|Mage|Healer|Tank) [A-Za-z0-9]+\\)")
private val REGEX_NAME = Regex("[A-Za-z0-9_]{1,16}")

object LeapMenu : Module("leap-menu", "Leap Menu", "5-grid ring-shaped leap menu.") {
  @Serializable
  enum class DungeonClass {
    ARCHER,
    BERSERK,
    MAGE,
    HEALER,
    TANK,
    UNKNOWN,
  }

  @Serializable
  private data class Player(
      val name: String,
      val dead: Boolean,
      val selectedClass: String,
  )

  private var players by state<List<Player?>>("$id/players") { listOf() }

  private val playerInfoMap = mutableMapOf<String, PlayerInfo>()

  private val playerClassMap = mutableMapOf<String, DungeonClass>()

  private val playerDeadMap = mutableMapOf<String, Boolean>()

  private fun getContainer(): ContainerScreen? {
    if (!enabled || mc.level == null) return null
    val screen = mc.screen as? ContainerScreen ?: return null
    return when (screen.title.string) {
      "Spirit Leap",
      "Teleport to Player" -> screen
      else -> null
    }
  }

  init {
    TickEvent {
      val container = getContainer() ?: return@TickEvent
      val inventory = container.menu

      mc.player!!.connection.listedOnlinePlayers.forEach { info ->
        if (info.profile.name in playerClassMap) {
          playerInfoMap[info.profile.name] = info
          return@forEach
        }

        val result =
            REGEX_TAB_NAME.matchEntire(
                mc.gui.tabList
                    .getNameForDisplay(info)
                    .string
                    .filter { it.code in 32..126 }
                    .replace(REGEX_IN_BRACKETS, "")
                    .trim()
            ) ?: return@forEach

        val name = result.groupValues[1]
        val className = result.groupValues[2].lowercase()
        val playerClass = DungeonClass.entries.firstOrNull { it.name.lowercase() == className }
        if (playerClass != null) {
          playerClassMap[name] = playerClass
        }
      }

      val playerSet = mutableListOf<String>()

      (9..17).forEach { slotId ->
        if (slotId >= inventory.slots.size) return@forEach
        val stack = inventory.getSlot(slotId).item ?: return@forEach
        if (stack.item != Items.PLAYER_HEAD) return@forEach
        var name = stack.displayName.string.trim()
        if (' ' in name) name = name.split(' ').last()
        name = name.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' || it == '_' }
        if (!name.matches(REGEX_NAME)) return@forEach
        playerSet += name
        playerDeadMap[name] =
            stack.getTooltipLines(Item.TooltipContext.EMPTY, mc.player, TooltipFlag.NORMAL).all {
              it.string != "Click to teleport!"
            }
      }

      val playerList =
          playerSet
              .toList()
              .sorted()
              .map { it to (playerClassMap[it] ?: DungeonClass.UNKNOWN) }
              .toMutableList()

      fun takeClass(theClass: DungeonClass): Pair<String, DungeonClass>? {
        return playerList.firstOrNull { it.second === theClass }?.also(playerList::remove)
      }

      val newPlayers =
          listOf(
                  takeClass(DungeonClass.ARCHER),
                  takeClass(DungeonClass.BERSERK),
                  takeClass(DungeonClass.MAGE),
                  takeClass(DungeonClass.HEALER),
                  takeClass(DungeonClass.TANK),
              )
              .map {
                (it ?: playerList.removeFirstOrNull())?.let { (name, theClass) ->
                  Player(
                      name,
                      playerDeadMap[name] ?: false,
                      theClass.name.lowercase(),
                  )
                }
              }

      if (newPlayers != players) {
        players = newPlayers
      }
    }

    WorldLoadEvent {
      playerInfoMap.clear()
      playerClassMap.clear()
      playerDeadMap.clear()
      players = listOf()
    }

    ScreenProxyEvent {
      val container = getContainer() ?: return@ScreenProxyEvent
      mutProxy = Proxy(container)
    }

    BrowserEvent {
      postJson<String>("module/$id/leap-to") { target ->
        if (getContainer() == null) {
          respond(404)
          return@postJson
        }
        ScheduleEvent {
          val container = getContainer() ?: return@ScheduleEvent
          val inventory = container.menu

          (9..17).forEach { slotId ->
            if (slotId >= inventory.slots.size) return@forEach
            val stack = inventory.getSlot(slotId).item ?: return@forEach
            if (stack.item != Items.PLAYER_HEAD) return@forEach
            var name = stack.displayName.string.trim()
            if (' ' in name) name = name.split(' ').last()
            name = name.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' || it == '_' }
            if (name == target) {
              middleClickContainer(inventory.containerId, slotId)
              return@ScheduleEvent
            }
          }
        }
        respond(200)
      }
    }
  }

  override val config = config {
    ::enabled {
      name = "Enabled"
      style = switch
    }
  }

  private class Proxy(val proxied: ContainerScreen) : BrowserScreen("Leap Menu", Gui.screenLayer) {
    override fun added() {
      open("${Gui.HOST}/#/screen/leap-menu")
    }

    override fun keyPressed(event: KeyEvent): Boolean {
      if (mc.options.keyInventory.matches(event)) {
        return proxied.keyPressed(event)
      }
      return super.keyPressed(event)
    }
  }
}
