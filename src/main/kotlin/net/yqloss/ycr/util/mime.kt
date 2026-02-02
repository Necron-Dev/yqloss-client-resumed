package net.yqloss.ycr.util

fun getMimeType(url: String) =
    when (url.substring(url.lastIndexOf(".") + 1).lowercase()) {
      "html",
      "htm" -> "text/html"
      "css" -> "text/css"
      "js" -> "application/javascript"
      "json" -> "application/json"
      "png" -> "image/png"
      "jpg",
      "jpeg" -> "image/jpeg"
      "gif" -> "image/gif"
      "svg" -> "image/svg+xml"
      "txt" -> "text/plain"
      "mp3" -> "audio/mpeg"
      "mp4" -> "video/mp4"
      else -> "text/html"
    }
