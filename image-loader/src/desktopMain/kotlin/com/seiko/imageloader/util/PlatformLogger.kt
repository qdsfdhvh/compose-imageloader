package com.seiko.imageloader.util

import java.io.PrintWriter
import java.io.StringWriter
import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.SimpleFormatter

actual abstract class PlatformLogger actual constructor() : Logger {
    private val logger = java.util.logging.Logger.getLogger("DebugAntilog::class.java.name").apply {
        level = Level.ALL
        useParentHandlers = false
        addHandler(
            ConsoleHandler().apply {
                level = Level.ALL
                formatter = SimpleFormatter()
            },
        )
    }

    private val tagMap: HashMap<LogPriority, String> = hashMapOf(
        LogPriority.VERBOSE to "[VERBOSE]",
        LogPriority.DEBUG to "[DEBUG]",
        LogPriority.INFO to "[INFO]",
        LogPriority.WARN to "[WARN]",
        LogPriority.ERROR to "[ERROR]",
        LogPriority.ASSERT to "[ASSERT]",
    )

    actual fun log(
        priority: LogPriority,
        tag: String,
        throwable: Throwable?,
        message: String,
    ) {
        val fullMessage = if (throwable != null) {
            "$message\n${throwable.stackTraceString}"
        } else {
            message
        }
        when (priority) {
            LogPriority.VERBOSE -> logger.finest(buildLog(priority, tag, fullMessage))
            LogPriority.DEBUG -> logger.fine(buildLog(priority, tag, fullMessage))
            LogPriority.INFO -> logger.info(buildLog(priority, tag, fullMessage))
            LogPriority.WARN -> logger.warning(buildLog(priority, tag, fullMessage))
            LogPriority.ERROR -> logger.severe(buildLog(priority, tag, fullMessage))
            LogPriority.ASSERT -> logger.severe(buildLog(priority, tag, fullMessage))
        }
    }

    private fun buildLog(priority: LogPriority, tag: String?, message: String): String {
        return "${tagMap[priority]} $tag - $message"
    }

    private val Throwable.stackTraceString
        get(): String {
            // DO NOT replace this with Log.getStackTraceString() - it hides UnknownHostException, which is
            // not what we want.
            val sw = StringWriter(256)
            val pw = PrintWriter(sw, false)
            printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }
}
