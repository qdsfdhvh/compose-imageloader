package com.seiko.imageloader.util

actual class DebugLogger actual constructor() : Logger {
    override fun log(priority: LogPriority, tag: String, throwable: Throwable?, message: String) {
        val fullMessage = if (throwable != null) {
            "$message\n${throwable.message}"
        } else {
            message
        }
        when (priority) {
            LogPriority.VERBOSE -> console.log("VERBOSE $tag : $fullMessage")
            LogPriority.DEBUG -> console.log("DEBUG $tag : $fullMessage")
            LogPriority.INFO -> console.info("INFO $tag : $fullMessage")
            LogPriority.WARN -> console.warn("WARNING $tag : $fullMessage")
            LogPriority.ERROR -> console.error("ERROR $tag : $fullMessage")
            LogPriority.ASSERT -> console.error("ASSERT $tag : $fullMessage")
        }
    }
}
