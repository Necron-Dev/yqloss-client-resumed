package net.yqloss.ycr.util

import java.nio.file.Files
import kotlin.io.path.Path
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("YCR/mime")

fun getMimeType(url: String) =
    try {
      Files.probeContentType(Path(url.substring(url.lastIndexOf('/') + 1))) ?: "text/html"
    } catch (exception: Exception) {
      logger.warn("failed to get mime type for url: $url", exception)
      "text/html"
    }
