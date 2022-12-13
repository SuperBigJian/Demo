plugins {
    id("convention.android.library")
    id("convention.android.hilt")
    id("convention.android.room")
    id("kotlinx-serialization")
    id("publish.maven")
}

android {
    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    api(libs.bundles.commonLibs)
    api(libs.bundles.httpLibs)
    api(libs.log.timber)
    api(libs.utils)
}