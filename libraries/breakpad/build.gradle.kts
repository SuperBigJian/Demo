plugins {
    id("convention.android.library")
    id("publish.maven")
}

android {

    defaultConfig {
        externalNativeBuild {
            cmake {
                //生成多个版本的so文件
                abiFilters.apply {
                    add("armeabi-v7a")
                    add("arm64-v8a")
                }
            }
        }
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
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
    api(project(":base:common"))
}