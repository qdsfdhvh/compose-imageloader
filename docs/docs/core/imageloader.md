# ImageLoader

`ImageLoader` structure is as follows, build by `ImageLoader {}`:

```kotlin
interface ImageLoader {
    fun async(request: ImageRequest): Flow<ImageAction>
}
```

`ImageAction` structure is as follows:

```kotlin
sealed interface ImageAction {
    sealed interface Loading : ImageAction
    sealed interface Success : ImageAction
    sealed interface Failure : ImageAction {
        val error: Throwable
    }
}

sealed interface ImageEvent : ImageAction.Loading {
    data object Start : ImageEvent
    data object StartWithMemory : ImageEvent
    data object StartWithDisk : ImageEvent
    data object StartWithFetch : ImageEvent
}

sealed interface ImageResult : ImageAction {
    data class OfBitmap() : ImageResult, ImageAction.Success
    data class OfImage() : ImageResult, ImageAction.Success
    data class OfPainter() :ImageResult, ImageAction.Success
    data class OfError(override val error: Throwable) : ImageResult, ImageAction.Failure
    data class OfSource() : ImageResult, ImageAction.Failure {
        override val error: Throwable
            get() = IllegalStateException("failure to decode image source")
    }
}
```

## Interceptor

This is the most center feature of `ImageLoader`, The loading of the entire image is implemented by the default 3 + 2 interceptors:

- **MappedInterceptor**
- MemoryCacheInterceptors
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
