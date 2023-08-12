import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.android.test)
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
    implementation(libs.androidx.junit)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.profileinstaller)
}

// workaround for baselineProfiles
tasks.matching { it.name == "pixel6proApi31NonMinifiedBenchmarkAndroidTest" }.configureEach {
    dependsOn(tasks.matching { it.name == "mergeNonMinifiedReleaseTestResultProtos" })
    dependsOn(tasks.matching { it.name == "collectNonMinifiedReleaseBaselineProfile" })
}
