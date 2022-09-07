plugins {
    id("common.android.application")
}

android {
    defaultConfig {
        applicationId = "com.cyaan.demo.hilt"
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
