package com.seiko.imageloader.util

enum class LogPriority {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    ASSERT,
}

interface Logger {

    fun isLoggable(priority: LogPriority): Boolean

    fun log(
        priority: LogPriority,
        tag: String,
        data: Any?,
        throwable: Throwable?,
        message: String,
    )

    companion object {
        val None = object : Logger {
            override fun isLoggable(priority: LogPriority): Boolean = false
            override fun log(
                priority: LogPriority,
                tag: String,
                data: Any?,
                throwable: Throwable?,
                message: String,
            ) = Unit
        }
    }
}

internal fun Logger.v(
    tag: String,
    data: Any,
    message: () -> String,
) = log(
    priority = LogPriority.VERBOSE,
    tag = tag,
    data = data,
    throwable = null,
    message = message,
)

internal fun Logger.d(
    tag: String,
    data: Any,
    message: () -> String,
) = log(
    priority = LogPriority.DEBUG,
    tag = tag,
    data = data,
    throwable = null,
    message = message,
)

internal fun Logger.i(
    tag: String,
    data: Any,
    message: () -> String,
) = log(
    priority = LogPriority.INFO,
    tag = tag,
    data = data,
    throwable = null,
    message = message,
)

internal fun Logger.w(
    tag: String,
    data: Any,
    throwable: Throwable,
    message: () -> String,
) = log(
    priority = LogPriority.WARN,
    tag = tag,
    data = data,
    throwable = throwable,
    message = message,
)

internal inline fun Logger.log(
    priority: LogPriority,
    tag: String,
    data: Any? = null,
    throwable: Throwable? = null,
    message: () -> String,
) {
    if (isLoggable(priority)) {
        log(priority, tag, data, throwable, message())
    }
}
