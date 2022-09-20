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

include(":base:common")
include(":base:commonUI")
include(":module:stock")
include(":libraries:breakpad")
include(":app")

include(":sample:compose")
include(":sample:hilt")
include(":sample:breakpad")
include(":sample:binder:remote")
include(":sample:binder:client")

include(":JavaKT")
