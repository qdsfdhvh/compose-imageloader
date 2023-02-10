package com.seiko.imageloader.util

open class DebugLogger(
    private val logPriority: LogPriority = LogPriority.DEBUG,
) : PlatformLogger() {

    override fun isLoggable(priority: LogPriority): Boolean = priority >= logPriority

    override fun log(
        priority: LogPriority,
        tag: String,
        data: Any?,
        throwable: Throwable?,
        message: String,
    ) {
        val fullMessage = buildString {
            if (data != null) {
                append("[image data] ")
                append(data.parseString())
                append('\n')
            }
            append("[message] ")
            append(message)
        }
        log(priority, tag, throwable, fullMessage)
    }

    open fun Any.parseString(maxLength: Int = 100): String {
        val rawString = if (this is String) this else toString()
        return if (rawString.length > maxLength) "${rawString.substring(0, maxLength)}..." else rawString
    }
}
