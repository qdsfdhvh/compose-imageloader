import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("app.android.test")
    id("app.kotlin.android")
    alias(libs.plugins.baselineProfile)
}

android {
    namespace = "com.seiko.imageloader.demo.baselineprofile"
    compileSdk = 34
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    defaultConfig {
        minSdk = 28
        targetSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    testOptions.managedDevices.devices {
        @Suppress("UnstableApiUsage")
        create<ManagedVirtualDevice>("pixel6Api32").apply {
            device = "Pixel 6"
            apiLevel = 32
            systemImageSource = "aosp"
        }
    }
    targetProjectPath = ":app:android"
}

baselineProfile {
    // This specifies the managed devices to use that you run the tests on. The default
    // is none.
    managedDevices += "pixel6Api32"

    // This enables using connected devices to generate profiles. The default is true.
    // When using connected devices, they must be rooted or API 33 and higher.
    useConnectedDevices = false

    // Set to true to see the emulator, useful for debugging. Only enabled locally
    enableEmulatorDisplay = false
}

dependencies {
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.espresso)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.profileinstaller)
}

androidComponents {
    onVariants { v ->
        val artifactsLoader = v.artifacts.getBuiltArtifactsLoader()
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { artifactsLoader.load(it)?.applicationId!! },
        )
    }
}
