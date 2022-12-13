// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.androidPlugin)
        classpath(libs.kotlinPlugin)
        classpath(libs.hiltPlugin)
        classpath(libs.kotlinSerializationPlugin)
        classpath(libs.superbigjian.convention)
        classpath(libs.superbigjian.publishPlugin)
    }
    repositories {
        maven { setUrl("https://depend.iflytek.com/artifactory/mvn-repo/") }
        maven { setUrl("https://depend.iflytek.com/artifactory/mvn-AndroidPublic-repo/") }

        mavenCentral()
        google()
    }
}