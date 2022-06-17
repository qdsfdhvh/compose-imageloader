# Compose ImageLoader
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.qdsfdhvh/image-loader/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.qdsfdhvh/image-loader)

Compose Image library for Kotlin Multiplatform.

## Setup

Add the dependency in your common module's commonMain sourceSet

```kotlin
kotlin {
    // ...
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.github.qdsfdhvh:image-loader:$last_version")
            }
        }
    }
}

```

## How to Use

```kotlin
@Composeable
fun Content() {
    CompositionLocalProvider(
        LocalImageLoader provides generateImageLoader(),
    ) {
        val url = "https://....."
        val painter = rememberAsyncImagePainter(url)
        Image(painter, null)
    }
}

fun generateImageLoader(): ImageLoader {
    return ImageLoaderBuilder().build()
}
```

## Thx

[Coil](https://github.com/coil-kt/coil)