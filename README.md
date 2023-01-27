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
                // api("io.github.qdsfdhvh:image-loader-extension-blur:$last_version")
            }
        }
        val jvmMain by getting {
            dependencies {
                // api("io.github.qdsfdhvh:image-loader-extension-imageio:$last_version")
            }
        }
    }
}

```

## How to Use

### 1.2.8 or Later (ing...)

#### ImageLoader

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

// in android
fun generateImageLoader(): ImageLoader {
    return ImageLoader(/* requestCoroutineContext = Dispatchers.IO */) {
        components {
            setupDefaultComponents(
                context,
                httpClient = httpClient,
            )
            // or
            // setupKtorComponents(httpClient)
            // setupBase64Components()
            // setupCommonComponents()
            // setupJvmComponents()
            // setupAndroidComponents(context, maxImageSize)
            // or
            // add(KtorUrlMapper())
            // add(KtorUrlKeyer())
            // add(KtorUrlFetcher.Factory(httpClient))
            // ....
        }
        interceptor {
            addInterceptor(DoSomthingInterceptor())
            memoryCache {
                // Set the max size to 25% of the app's available memory.
                maxSizePercent(context, 0.25)
            }
            diskCache {
                directory(context.cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
            // or
            // useDefaultInterceptors = false
            // addInterceptors(
            //     listOf(
            //         DoSomthingInterceptor(),
            //         MappedInterceptor(),
            //         MemoryCacheInterceptor(),
            //         DecodeInterceptor(),
            //         DiskCacheInterceptor(),
            //         FetchInterceptor(),
            //     )
            // )
        }
    }
}
```

#### ImageRequest

```kotlin
val imageRequest = ImageRequest {
    data(url)
    components {
        ...
    }
    addInterceptor(DoSomthingInterceptor())
    extra {
        set("key_int", 11)
    }
}
val newImageRequest = newBuilder { 
    ...
}
```

### Before 1.2.8

`LocalImageLoader` has no default value, must be configured on each platform, and configuration is similar to `coil`.

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