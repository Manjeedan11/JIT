pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()


    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://atlas.microsoft.com/sdk/android")
        maven("https://jitpack.io")
        jcenter()

    }
}

rootProject.name = "AIT"
include(":app")
