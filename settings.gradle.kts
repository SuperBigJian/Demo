pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven(url ="https://maven.aliyun.com/nexus/content/groups/public/" )
        maven(url ="https://maven.aliyun.com/repository/public/" )
        maven(url ="https://maven.aliyun.com/repository/central" )
        maven(url ="https://maven.aliyun.com/repository/google" )
        maven(url ="https://maven.aliyun.com/repository/gradle-plugin" )

        mavenCentral()
        gradlePluginPortal()
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url ="https://maven.aliyun.com/nexus/content/groups/public/" )
        maven(url ="https://maven.aliyun.com/repository/public/" )
        maven(url ="https://maven.aliyun.com/repository/central" )
        maven(url ="https://maven.aliyun.com/repository/google" )
        maven(url ="https://maven.aliyun.com/repository/gradle-plugin" )

        mavenCentral()
        google()
    }
}
rootProject.name = "Demo"

include (":base:common")
include (":base:commonUI")
include (":module:stock")
include (":libraries:breakpad")
include (":app")

include (":sample:compose")
include (":sample:hilt")
include (":sample:breakpad")
include (":sample:binder:remote")
include (":sample:binder:client")

//include (":JavaKT")

