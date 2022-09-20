pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}
rootProject.name = "Demo"

include(":JavaKT")

include(":base:common")
include(":base:commonUI")
include(":libraries:breakpad")
include(":module:stock")
include(":app")

include(":sample:compose")
include(":sample:hilt")
include(":sample:breakpad")
include(":sample:capteure")
include(":sample:binder:remote")
include(":sample:binder:client")
include(":sample:capture")


