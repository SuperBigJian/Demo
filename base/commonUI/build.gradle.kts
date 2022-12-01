plugins {
    id("convention.android.library")
    id("convention.android.hilt")
    id("convention.android.compose")
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