plugins {
    id("convention.android.application")
}

android {
    defaultConfig {
        applicationId = "com.cyaan.demo.breakpad"

        ndk {
            abiFilters.apply {
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

dependencies {
    implementation(project(":base:commonUI"))
    implementation("io.github.superbigjian.libs:breakpad:1.0.1")
}