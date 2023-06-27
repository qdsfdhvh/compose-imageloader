# Image IO

## Use

```diff
val imageLoader = ImageLoader {
    // ...
    components {
+        add(ImageIODecoder.Factory())
    }
}
```

Only support in jvm target.