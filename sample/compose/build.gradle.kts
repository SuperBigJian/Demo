plugins {
    id("convention.android.application")
}

android {
    defaultConfig {
        applicationId = "com.cyaan.demo.compose"
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}