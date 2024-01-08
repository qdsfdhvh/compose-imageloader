package com.seiko.imageloader.component

import com.seiko.imageloader.component.mapper.NSURLToUriMapper

fun ComponentRegistryBuilder.setupAppleComponents() {
    add(NSURLToUriMapper())
}
