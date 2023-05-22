plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
}

dependencies {
    implementation(libs.bundles.plugins)
}

gradlePlugin {
    plugins {
        create("build-logic") {
            id = "build-logic"
            implementationClass = "BuildLogic"
        }
        create("project-kmp") {
            id = "project-kmp"
            implementationClass = "ProjectKmpPlugin"
        }
    }
}
