plugins {
    id("common.android.library")
    id("common.android.hilt")
    id("common.android.compose")
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
    api(project(":base:common"))
}