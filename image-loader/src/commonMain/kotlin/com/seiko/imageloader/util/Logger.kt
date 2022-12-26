package com.seiko.imageloader.util

enum class LogPriority {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    ASSERT;
}

interface Logger {

    fun isLoggable(priority: LogPriority) = priority >= LogPriority.DEBUG

    fun log(
        priority: LogPriority,
        tag: String,
        data: Any?,
        throwable: Throwable?,
        message: String
    )

    fun Any.parseString(maxLength: Int = 100): String {
        val rawString = if (this is String) this else toString()
        return if (rawString.length > maxLength) "${rawString.substring(0, maxLength)}..." else rawString
    }

    companion object : Logger {

        private val baseArray = mutableListOf<Logger>()

        internal fun base(vararg logger: Logger) {
            baseArray.addAll(logger)
        }

        override fun isLoggable(priority: LogPriority): Boolean {
            return baseArray.any { it.isLoggable(priority) }
        }

        override fun log(priority: LogPriority, tag: String, data: Any?, throwable: Throwable?, message: String) {
            baseArray.forEach { it.log(priority, tag, data, throwable, message) }
        }
    }
}

expect class DebugLogger() : Logger

internal inline fun logv(
    tag: String,
    data: Any,
    noinline message: () -> String
) = log(
    tag = tag,
    data = data,
    priority = LogPriority.VERBOSE,
    throwable = null,
    message = message,
)

internal inline fun logd(
    tag: String,
    data: Any,
    noinline message: () -> String
) = log(
    tag = tag,
    data = data,
    priority = LogPriority.DEBUG,
    throwable = null,
    message = message,
)

internal inline fun logi(
    tag: String,
    data: Any,
    noinline message: () -> String
) = log(
    tag = tag,
    data = data,
    priority = LogPriority.INFO,
    throwable = null,
    message = message,
)

internal inline fun logw(
    tag: String,
    data: Any,
    throwable: Throwable,
    noinline message: () -> String
) = log(
    tag = tag,
    data = data,
    priority = LogPriority.WARN,
    throwable = throwable,
    message = message,
)

internal inline fun log(
    tag: String,
    data: Any? = null,
    priority: LogPriority = LogPriority.DEBUG,
    throwable: Throwable? = null,
    message: () -> String
) {
    if (Logger.isLoggable(priority)) {
        Logger.log(priority, tag, data, throwable, message())
    }
}
