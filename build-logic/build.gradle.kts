plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
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
