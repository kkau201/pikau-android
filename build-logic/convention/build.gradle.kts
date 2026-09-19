plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "pikau.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "pikau.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("jvmLibrary") {
            id = "pikau.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = "pikau.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "pikau.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
    }
}