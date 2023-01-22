package com.seiko.imageloader.util

expect abstract class PlatformLogger() : Logger {
    fun log(priority: LogPriority, tag: String, throwable: Throwable?, message: String)
}
