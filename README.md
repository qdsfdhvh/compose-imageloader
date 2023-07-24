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
@Composable
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
            memoryCacheConfig {
                // Set the max size to 25% of the app's available memory.
                maxSizePercent(context, 0.25)
            }
            diskCacheConfig {
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

#### Memory/Disk cache support on all platforms
To enable caching on all platforms you have to setup custom ImageLoader
```kotlin
internal expect fun ComponentRegistryBuilder.setupDefaultComponents()
internal expect fun getImageCacheDirectoryPath(): Path

private fun generateImageLoader(
    memCacheSize: Int = 32 * 1024 * 1024, //32MB
    diskCacheSize: Int = 512 * 1024 * 1024 //512MB
) = ImageLoader {
    interceptor {
        memoryCacheConfig {
            maxSizeBytes(memCacheSize)
        }
        diskCacheConfig {
            directory(getImageCacheDirectoryPath())
            maxSizeBytes(diskCacheSize.toLong())
        }
    }
    components {
        setupDefaultComponents()
    }
}

//actuals on the platforms:
//Android
actual fun ComponentRegistryBuilder.setupDefaultComponents() = this.setupDefaultComponents(App.context)
actual fun getImageCacheDirectoryPath(): Path = App.context.cacheDir.absolutePath.toPath()

//iOS
actual fun ComponentRegistryBuilder.setupDefaultComponents() = this.setupDefaultComponents()
actual fun getImageCacheDirectoryPath(): Path {
    val cacheDir = NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true
    ).first() as String
    return (cacheDir + "/media").toPath()
}

//Desktop
actual fun ComponentRegistryBuilder.setupDefaultComponents() = this.setupDefaultComponents()
actual fun getImageCacheDirectoryPath(): Path = "media/".toPath()
```
And provide it to composition
```kotlin
@Composable
fun App() {
    CompositionLocalProvider(
        LocalImageLoader provides generateImageLoader(),
    ) {
        //your app here
    }
}
```

### Before 1.2.8

`LocalImageLoader` has no default value, must be configured on each platform, and configuration is similar to `coil`.

```kotlin
@Composable
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

---

###### Look for a remote job

I'm sorry to put the job hunting here, if you have a remote position about Android, feel free to contact me at [seiko_des@outlook.com](mailto:seiko_des@outlook.com).

PS: My English is not good.
