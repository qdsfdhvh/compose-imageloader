# Quick Start


## Display Image

Just use it like this for display image:

```kotlin
val painter = rememberAsyncImagePainter("https://..")
Image(
    painter = painter,
    contentDescription = "image",
)
```

But the default `Imageloader` will reload when it's displayed, is not friendly for `https` link.

So it is recommended to customise `ImageLoader` and configure the cache.

## Memory & Disk Cache

Custom `ImageLoader` requires the help of `CompositionLocalProvider`.

```diff
setContent {
    CompositionLocalProvider(
+        LocalImageLoader provides generateImageLoader(), // or remember { generateImageLoader() }
    ) {
        // App()
    }
}

// Android
fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        // ...
        interceptor {
+            memoryCacheConfig {
+                maxSizePercent(applicationContext, 0.25)
+            }
+            diskCacheConfig {
+                directory(cacheDir.resolve("image_cache").toOkioPath())
+                maxSizeBytes(512L * 1024 * 1024) // 512MB
+            }
        }
    }
}

// Desktop
fun generateImageLoader(): ImageLoader { 
    return ImageLoader {
        // ...
        interceptor {
+            memoryCacheConfig {
+                maxSizePercent(0.25)
+            }
+            diskCacheConfig {
                // about getCacheDir() see demo
+                directory(getCacheDir().toOkioPath().resolve("image_cache"))
+                maxSizeBytes(512L * 1024 * 1024) // 512MB
+            }
        }
    }
}

// IOS & Macos
fun generateImageLoader(): ImageLoader { 
    return ImageLoader {
        // ...
        interceptor {
+            memoryCacheConfig {
+                maxSizePercent(0.25)
+            }
+            diskCacheConfig {
                // about getCacheDir() see demo
+                directory(getCacheDir().toPath().resolve("image_cache"))
+                maxSizeBytes(512L * 1024 * 1024) // 512MB
+            }
        }
    }
}

// Js
fun generateImageLoader(): ImageLoader { 
    return ImageLoader {
        // ...
        interceptor {
+            memoryCacheConfig {
+                maxSizePercent(0.25)
+            }
        // At the moment I don't know how to configure the disk cache in js either
        diskCacheConfig(FakeFileSystem()) {
                directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY)
                maxSizeBytes(256L * 1024 * 1024) // 256MB
            }
        }
    }
}
```