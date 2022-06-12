plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.0")
    implementation(kotlin("gradle-plugin", version = "1.6.21"))
}