package net.yqloss.ycr.util

import net.minecraft.world.inventory.ClickType
import net.yqloss.ycr.mc

fun middleClickContainer(containerId: Int, slotId: Int) {
  mc.gameMode?.handleInventoryMouseClick(containerId, slotId, 2, ClickType.CLONE, mc.player)
}
