package net.yqloss.ycr.util

import org.slf4j.Logger

inline fun runLogging(logger: Logger, actionName: String = "runLogging", action: () -> Unit) {
  try {
    return action()
  } catch (exception: Exception) {
    logger.warn("error during $actionName", exception)
  }
}
