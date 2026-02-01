package net.yqloss.ycr.module

interface Module {
  val id: String

  val name: String

  val description: String

  val enabled: Boolean
}
