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

    fun isLoggable(priority: LogPriority): Boolean

    fun log(
        priority: LogPriority,
        tag: String,
        data: Any?,
        throwable: Throwable?,
        message: String
    )

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

internal fun logv(
    tag: String,
    data: Any,
    message: () -> String
) = log(
    tag = tag,
    data = data,
    priority = LogPriority.VERBOSE,
    throwable = null,
    message = message,
)

internal fun logd(
    tag: String,
    data: Any,
    message: () -> String
) = log(
    tag = tag,
    data = data,
    priority = LogPriority.DEBUG,
    throwable = null,
    message = message,
)

internal fun logi(
    tag: String,
    data: Any,
    message: () -> String
) = log(
    tag = tag,
    data = data,
    priority = LogPriority.INFO,
    throwable = null,
    message = message,
)

internal fun logw(
    tag: String,
    data: Any,
    throwable: Throwable,
    message: () -> String
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
