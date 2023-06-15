# ImageLoader

## Use

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

## Components

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

## Interceptor

```kotlin
ImageLoader {
    interceptor {
        useDefaultInterceptors = true
        // add custom interceptor
        add(Interceptor)
    }
}
```

default interceptor sequence:

`MappedInterceptor` -> `MemoryCacheInterceptor` -> `DecodeInterceptor` -> `DiskCacheInterceptor` -> `FetchInterceptor`.

