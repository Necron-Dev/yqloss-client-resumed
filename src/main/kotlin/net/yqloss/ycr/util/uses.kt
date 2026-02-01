package net.yqloss.ycr.util

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class UsesContext(
    private val resourceList: ArrayDeque<AutoCloseable>,
) {
  fun <T : AutoCloseable> use(resource: T) = resource.also(resourceList::add)

  fun defer(cleanup: () -> Unit) {
    resourceList += AutoCloseable { cleanup() }
  }

  val <T : AutoCloseable> T.use
    get() = use(this)

  infix fun <T> T.defer(cleanup: (T) -> Unit) = also { this@UsesContext.defer { cleanup(this) } }
}

data class ResourceClosureException(
    val failures: List<Pair<Any?, Exception>>,
) : Exception()

inline fun <R> uses(function: UsesContext.() -> R): R {
  contract { callsInPlace(function, InvocationKind.EXACTLY_ONCE) }

  val resourceList = ArrayDeque<AutoCloseable>()

  try {
    return UsesContext(resourceList).function()
  } finally {
    val exceptionList = mutableListOf<Pair<Any?, Exception>>()
    resourceList.asReversed().forEach { resource ->
      try {
        resource.close()
      } catch (exception: Exception) {
        exceptionList.add(resource to exception)
      }
    }
    exceptionList.isEmpty() || throw ResourceClosureException(exceptionList)
  }
}
