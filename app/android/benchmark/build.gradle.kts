import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.androidTest)
    alias(libs.plugins.baselineProfile)
    kotlin("android")
}

android {
    namespace = "com.seiko.imageloader.demo.benchmark"
    compileSdk = Versions.Android.compile
    defaultConfig {
        minSdk = 28
        targetSdk = Versions.Android.target
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // TODO temporary until AGP 8.2, which no longer requires this.
        //  This is because when we update baseline profiles, we do them on emulators but they
        //  run all tests.
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }
    compileOptions {
        sourceCompatibility = Versions.Java.source
        targetCompatibility = Versions.Java.target
    }
    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
    }
    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel6Api33").apply {
            device = "Pixel 6"
            apiLevel = 33
            systemImageSource = "google"
        }
    }
    targetProjectPath = ":app:android"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

baselineProfile {
    // This specifies the managed devices to use that you run the tests on. The default
    // is none.
    managedDevices += "pixel6Api33"

    // This enables using connected devices to generate profiles. The default is true.
    // When using connected devices, they must be rooted or API 33 and higher.
    useConnectedDevices = false

    // Set to true to see the emulator, useful for debugging. Only enabled locally
    // enableEmulatorDisplay = false
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.profileinstaller)
}
