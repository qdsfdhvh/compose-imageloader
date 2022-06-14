package com.seiko.imageloader.util

expect class LockObject()

expect inline fun <R> synchronized(lock: LockObject, block: () -> R): R
