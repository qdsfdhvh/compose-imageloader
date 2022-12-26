package com.seiko.imageloader.util

actual class DebugLogger actual constructor() : Logger {
    override fun log(priority: LogPriority, tag: String, data: Any?, throwable: Throwable?, message: String) {
        val fullMessage = buildString {
            if (data != null) {
                append("data:")
                append(data.parseString())
                append('\n')
            }
            append(message)
            if (throwable != null) {
                append('\n')
                append(throwable.message)
            }
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
