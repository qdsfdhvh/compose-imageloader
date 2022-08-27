config.resolve = {
    // https://webpack.js.org/configuration/resolve/#resolvealias
    fallback: {
        path: require.resolve("path-browserify"),
        os: require.resolve("os-browserify/browser"),
        fs: require.resolve("fs-extra"),
        stream: require.resolve("stream-browserify"),
        constants: require.resolve("constants-browserify"),
        process: require.resolve('process/browser'),
    },
};