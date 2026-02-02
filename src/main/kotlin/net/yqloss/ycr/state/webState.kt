package net.yqloss.ycr.state

import net.yqloss.ycr.event.BrowserEvent
import net.yqloss.ycr.gui.system.get
import net.yqloss.ycr.gui.system.respondJson
import net.yqloss.ycr.state.system.State
import net.yqloss.ycr.state.system.States

inline fun <reified T> webState(id: String, crossinline value: BrowserEvent.() -> T): State {
  States.allStates += id

  BrowserEvent { get("state/get/$id") { respondJson(value()) } }

  return object : State {
    override val id = id
  }
}
