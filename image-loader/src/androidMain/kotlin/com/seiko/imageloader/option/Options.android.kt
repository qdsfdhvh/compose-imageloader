package com.seiko.imageloader.option

import android.content.Context

fun OptionsBuilder.androidContext(context: Context) {
    extra {
        set(KEY_ANDROID_CONTEXT, context)
    }
}

val Options.androidContext: Context
    get() = requireNotNull(extra[KEY_ANDROID_CONTEXT] as? Context) {
        "not androidContext in options extra"
    }

private const val KEY_ANDROID_CONTEXT = "KEY_ANDROID_CONTEXT"
