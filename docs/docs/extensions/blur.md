# Blur

## Use

```diff
val imageLoader = ImageLoader {
    // ...
    interceptor {
+        addInterceptor(BlurInterceptor())
    }
}
val request = ImageRequest {
    data("https://...")
+    blur(blurRadius = 15)
}
// ...
```