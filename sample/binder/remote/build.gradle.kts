plugins {
    id("convention.android.application")
}

android {
    defaultConfig {
        applicationId = "com.cyaan.demo.binder.remote"
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = ".debug"
        }
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}