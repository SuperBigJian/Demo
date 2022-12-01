plugins {
    id("convention.android.module")
}

android {


    buildTypes {
        val debug by getting {

        }
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}