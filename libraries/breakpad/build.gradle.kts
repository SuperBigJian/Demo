plugins {
    id("common.android.library")
}

android {

    defaultConfig {
        consumerProguardFile("consumer-rules.pro")
        
        ndk {
            abiFilters.apply {
                add("armeabi-v7a")
                add("arm64-v8a")
            }
        }
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }
}