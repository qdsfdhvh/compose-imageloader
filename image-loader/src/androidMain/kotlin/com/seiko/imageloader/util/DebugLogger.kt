package com.seiko.imageloader.util

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Integer.min

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
                append(throwable.stackTraceString)
            }
        }

        val length = fullMessage.length
        if (length <= MAX_LOG_LENGTH) {
            // Fast path for small messages which can fit in a single call.
            if (priority == LogPriority.ASSERT) {
                Log.wtf(tag, fullMessage)
            } else {
                Log.println(priority.level, tag, fullMessage)
            }
            return
        }

        // Slow path: Split by line, then ensure each line can fit into Log's maximum length.
        // TODO use lastIndexOf instead of indexOf to batch multiple lines into single calls.
        var i = 0
        while (i < length) {
            var newline = fullMessage.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = min(newline, i + MAX_LOG_LENGTH)
                val part = fullMessage.substring(i, end)
                if (priority == LogPriority.ASSERT) {
                    Log.wtf(tag, part)
                } else {
                    Log.println(priority.level, tag, part)
                }
                i = end
            } while (i < newline)
            i++
        }
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

    private val LogPriority.level: Int
        get() = when (this) {
            LogPriority.VERBOSE -> Log.VERBOSE
            LogPriority.DEBUG -> Log.DEBUG
            LogPriority.INFO -> Log.INFO
            LogPriority.WARN -> Log.WARN
            LogPriority.ERROR -> Log.ERROR
            LogPriority.ASSERT -> Log.ASSERT
        }

    companion object {
        private const val MAX_LOG_LENGTH = 4000
    }
}
