package net.yqloss.ycr.state

import net.yqloss.ycr.event.BrowserEvent
import net.yqloss.ycr.gui.get
import net.yqloss.ycr.gui.respondJson

inline fun <reified T> webState(id: String, crossinline value: BrowserEvent.() -> T) {
  States.allStates += id

  BrowserEvent { get("state/get/$id") { respondJson(value()) } }
}
