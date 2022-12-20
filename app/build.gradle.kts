plugins {
    id("convention.android.application")
    id("convention.android.test")
    id("common.flavors")
}

android {
    defaultConfig {
        applicationId = "com.example.demo"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
dependencies {
//    api(project(":base:commonUI"))
    api("io.github.superbigjian.base:common-ui:1.0.0")
}
