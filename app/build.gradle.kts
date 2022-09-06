plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.example.demo"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        Desaysv_new {
            storeFile file ("keystore\\Desaysv\\desaysv-new.keystore")
            storePassword = "android"
            keyAlias = "desaysv"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            minifyEnabled = false
            signingConfig = signingConfigs.Desaysv_new
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation project (path(":base:commonUI"))
    implementation(cfg.libs.injectLib.core)
    kapt(cfg.libs.injectLib.compiler)
}