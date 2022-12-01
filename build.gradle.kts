// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.superbigjian.convention)
        classpath(libs.androidPlugin)
        classpath(libs.kotlinPlugin)
        classpath(libs.kotlinSerializationPlugin)
        classpath(libs.hiltPlugin)
    }
    repositories {
        mavenCentral()
        google()
    }
}