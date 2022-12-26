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

    fun isLoggable(priority: LogPriority) = true

    fun log(
        priority: LogPriority,
        tag: String,
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

        override fun log(priority: LogPriority, tag: String, throwable: Throwable?, message: String) {
            baseArray.forEach { it.log(priority, tag, throwable, message) }
        }
    }
}

expect class DebugLogger() : Logger

internal inline fun log(
    tag: String,
    throwable: Throwable? = null,
    priority: LogPriority = LogPriority.DEBUG,
    message: () -> String
) {
    if (Logger.isLoggable(priority)) {
        Logger.log(priority, tag, throwable, message())
    }
}
