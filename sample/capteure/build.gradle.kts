plugins {
    id("common.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    defaultConfig {
        applicationId = "com.cyaan.demo.capture"
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
dependencies {
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.4.+")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
}
