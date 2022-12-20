plugins {
    id("convention.android.library")
    id("convention.android.hilt")
    id("convention.android.compose")
    id("publish.maven")
}
android {
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
//    api(project(":base:common"))
    api("io.github.superbigjian.base:common:1.0.0")
}