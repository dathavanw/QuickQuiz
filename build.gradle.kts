// build.gradle.kts (Project: BTL)

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }

    repositories {
        google()
        mavenCentral()
    }
}

plugins {

    alias(libs.plugins.android.application) apply false // tương ứng với `android-application`
    alias(libs.plugins.android.library) apply false     // tương ứng với `android-library`
    alias(libs.plugins.kotlin.android) apply false      // tương ứng với `kotlin-android`
}
