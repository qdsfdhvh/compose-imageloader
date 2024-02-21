import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("app.android.test")
    id("app.kotlin.android")
    alias(libs.plugins.baselineProfile)
}

android {
    namespace = "com.seiko.imageloader.demo.benchmark"
    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        create<ManagedVirtualDevice>("pixel6proApi31").apply {
            device = "Pixel 6 Pro"
            apiLevel = 31
            systemImageSource = "aosp"
        }
    }
    targetProjectPath = ":app:android"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

baselineProfile {
    // This specifies the managed devices to use that you run the tests on. The default
    // is none.
    managedDevices += "pixel6proApi31"

    // This enables using connected devices to generate profiles. The default is true.
    // When using connected devices, they must be rooted or API 33 and higher.
    useConnectedDevices = false

    // Set to true to see the emulator, useful for debugging. Only enabled locally
    enableEmulatorDisplay = false
}

dependencies {
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.profileinstaller)
}
