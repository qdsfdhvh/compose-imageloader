# ImageLoader

`ImageLoader` structure is as follows, build by `ImageLoader {}`:

```kotlin
interface ImageLoader {
    fun async(request: ImageRequest): Flow<ImageAction>
}
```

`ImageAction` structure is as follows:

```kotlin
sealed interface ImageAction

sealed interface ImageEvent : ImageAction {
    object Start : ImageEvent
    object StartWithMemory : ImageEvent
    object StartWithDisk : ImageEvent
    object StartWithFetch : ImageEvent
    data class Progress(val progress: Float) : ImageEvent
}

sealed interface ImageResult : ImageAction {
    data class Source() : ImageResult
    data class Bitmap() : ImageResult
    data class Image() : ImageResult
    data class Painter() : ImageResult
    data class Error() : ImageResult
}
```

## Interceptor

This is the most center feature of `ImageLoader`, The loading of the entire image is implemented by the default 3 + 2 interceptors:

- **MappedInterceptor**
- MemoryCacheInterceptor 
- **DecodeInterceptor**
- DiskCacheInterceptor
- **FetchInterceptor**

```
ImageLoader {
    interceptor {
        useDefaultInterceptors = true
        // add custom interceptor, before of default
        add(Interceptor)
    }
}
```

## Components

Add implementations for the `MappedInterceptor` above, and `Keyer` is generating the key value for `MemoryCache` & `DiskCache`.

```kotlin
ImageLoader {
    components {
        add(Mapper)
        add(Keyer)
        add(Fetcher)
        add(Decoder)
    }
}
```

## Logger

```kotlin
ImageLoader {
    imageScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    logger = object : Logger {
        override fun isLoggable(priority: LogPriority): Boolean = false
        override fun log(
            priority: LogPriority,
            tag: String,
            data: Any?,
            throwable: Throwable?,
            message: String,
        ) {
            // print log
        }
    }
}
```

## Options

```kotlin
ImageLoader {
    options {
        allowInexactSize = false
        premultipliedAlpha = true
        retryIfDiskDecodeError = true
        imageConfig = Options.ImageConfig.ARGB_8888
        scale = Scale.AUTO
        sizeResolver = SizeResolver.Unspecified
        memoryCachePolicy = CachePolicy.ENABLED
        diskCachePolicy = CachePolicy.ENABLED
        playAnimate = true
        repeatCount = Options.REPEAT_INFINITE
        extra {
            put("key", "value")
        }
    }
}
```
