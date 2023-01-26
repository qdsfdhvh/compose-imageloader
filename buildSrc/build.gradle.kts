plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.0")
    implementation(kotlin("gradle-plugin", version = "1.8.0"))
}
