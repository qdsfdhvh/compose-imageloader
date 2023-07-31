# Compose ImageLoader
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.qdsfdhvh/image-loader/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.qdsfdhvh/image-loader)

Compose Image library for Kotlin Multiplatform.

## Setup

Add the dependency in your common module's commonMain sourceSet


```diff title="build.gradle.kts"
kotlin {
    android()
    ios()
    // ...

    sourceSets {
        val commonMain by getting {
            dependencies {
+                api("io.github.qdsfdhvh:image-loader:1.6.3")
                // optional - Moko Resources Decoder
+                api("io.github.qdsfdhvh:image-loader-extension-moko-resources:1.6.3")
                // optional - Blur Interceptor (only support bitmap)
+                api("io.github.qdsfdhvh:image-loader-extension-blur:1.6.3")
            }
        }
        val jvmMain by getting {
            dependencies {
                // optional - ImageIO Decoder
+                api("io.github.qdsfdhvh:image-loader-extension-imageio:1.6.3")
            }
        }
    }
}
```

## How to Use

### Display Image

```kotlin
val painter = rememberImagePainter("https://..")
Image(
    painter = painter,
    contentDescription = "image",
)
```

PS: default `Imageloader` will reload when it's displayed, is not friendly for `https` link, so it is recommended to custom `ImageLoader` and configure the cache.

### Custom ImageLoader

I configure the `Imageloader {}` on each platform, you also can configure it in the `commonMain` like [Tachidesk-JUI](https://github.com/Suwayomi/Tachidesk-JUI/blob/master/presentation/src/commonMain/kotlin/ca/gosyer/jui/ui/base/image/ImageLoaderProvider.kt).

```kotlin
@Composable
fun Content() {
    CompositionLocalProvider(
        LocalImageLoader provides remember { generateImageLoader() },
    ) {
        val painter = rememberImagePainter("https://..")
        Image(
            painter = painter,
            contentDescription = "image",
        )
    }
}
```

#### in Android

```kotlin title="MainActivity.kt"
fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        options {
            androidContext(applicationContext)
        }
        components {
            setupDefaultComponents()
        }
        interceptor {
            memoryCacheConfig {
                // Set the max size to 25% of the app's available memory.
                maxSizePercent(context, 0.25)
            }
            diskCacheConfig {
                directory(context.cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}
```

#### in Jvm

```kotlin title="Main.kt"
fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
            diskCacheConfig {
                directory(getCacheDir().toOkioPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

// about currentOperatingSystem, see app
private fun getCacheDir() = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "$ApplicationName/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/$ApplicationName")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/$ApplicationName")
    else -> throw IllegalStateException("Unsupported operating system")
}
```

#### in iOS

```kotlin
fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
       interceptor {
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
            diskCacheConfig {
                directory(getCacheDir().toPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

private fun getCacheDir(): String {
    return NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true,
    ).first() as String
}

```

for more platform targets, see [app](app/).

#### ImageRequest

```kotlin
val imageRequest = ImageRequest {
    data(url)
    addInterceptor(DoSomthingInterceptor())
    components {
        // ...
    }
    extra {
        set("key_int", 11)
    }
}
val newImageRequest = imageRequest.newBuilder { 
    // ...
}
```

### Before 1.2.8

`LocalImageLoader` has no default value, you must be configured on each platform, and configuration is similar to `coil`.

```kotlin
@Composable
fun Content() {
    CompositionLocalProvider(
        LocalImageLoader provides remember { generateImageLoader() },
    ) {
        val painter = rememberAsyncImagePainter("https://.....")
        Image(
            painter = painter,
            contentDescription = "image",
        )
    }
}

fun generateImageLoader(): ImageLoader {
    return ImageLoaderBuilder().build()
}
```

## Thx

[Coil](https://github.com/coil-kt/coil)

---

###### Look for a remote job

I'm sorry to put the job hunting here, if you have a remote position about Android, feel free to contact me at [seiko_des@outlook.com](mailto:seiko_des@outlook.com).

PS: My English is not good.
