# Moko Resources

## Use

```diff
val imageLoader = ImageLoader {
    // ...
    components {
+        add(MokoResourceFetcher.Factory())
    }
    
    // in Android target
    options {
+        androidContext(applicationContext)
    }
}
// ...
```

Support `AssetResource`, `ColorResource`, `FileResource`, `ImageResource`.
