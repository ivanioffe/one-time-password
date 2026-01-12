// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.dokka") version "2.1.0"
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint) apply false
}

allprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    dokka(project(":compose"))
    dokka(project(":core"))
}

dokka {
    moduleVersion.set(libs.versions.oneTimePassword.get())
}
