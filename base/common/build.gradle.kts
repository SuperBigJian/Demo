plugins {
    id("common.android.library")
    id("common.android.hilt")
    id("common.android.room")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
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