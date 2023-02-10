package com.seiko.imageloader.util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

actual abstract class PlatformLogger actual constructor() : Logger {

    private val dateFormatter = NSDateFormatter().apply {
        dateFormat = "MM-dd HH:mm:ss.SSS"
    }

    private val tagMap: HashMap<LogPriority, String> = hashMapOf(
        LogPriority.VERBOSE to "💜 VERBOSE",
        LogPriority.DEBUG to "💚 DEBUG",
        LogPriority.INFO to "💙 INFO",
        LogPriority.WARN to "💛 WARN",
        LogPriority.ERROR to "❤️ ERROR",
        LogPriority.ASSERT to "💞 ASSERT",
    )

    actual fun log(
        priority: LogPriority,
        tag: String,
        throwable: Throwable?,
        message: String,
    ) {
        println(buildLog(priority, tag, throwable, message))
    }

    private fun buildLog(priority: LogPriority, tag: String, throwable: Throwable?, message: String): String {
        val baseLogString = "${getCurrentTime()} ${tagMap[priority]} $tag - $message"
        return if (throwable != null) {
            "$baseLogString\n${throwable.stackTraceToString()}"
        } else {
            baseLogString
        }
    }

    private fun getCurrentTime() = dateFormatter.stringFromDate(NSDate())
}
