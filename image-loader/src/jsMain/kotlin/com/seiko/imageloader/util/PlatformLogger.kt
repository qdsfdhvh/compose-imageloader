package com.seiko.imageloader.util

actual abstract class PlatformLogger actual constructor() : Logger {
    actual fun log(
        priority: LogPriority,
        tag: String,
        throwable: Throwable?,
        message: String,
    ) {
        when (priority) {
            LogPriority.VERBOSE -> console.log("VERBOSE $tag : $message")
            LogPriority.DEBUG -> console.log("DEBUG $tag : $message")
            LogPriority.INFO -> console.info("INFO $tag : $message")
            LogPriority.WARN -> console.warn("WARNING $tag : $message")
            LogPriority.ERROR -> console.error("ERROR $tag : $message")
            LogPriority.ASSERT -> console.error("ASSERT $tag : $message")
        }
    }
}
