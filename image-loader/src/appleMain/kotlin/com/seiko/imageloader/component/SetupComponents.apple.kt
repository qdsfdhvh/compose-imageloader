package com.seiko.imageloader.component

import com.seiko.imageloader.component.mapper.NSURLToPathMapper

fun ComponentRegistryBuilder.setupAppleComponents() {
    add(NSURLToPathMapper())
}
