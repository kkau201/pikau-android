import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.kaylakautai.buildlogic.GenerateOpenSourceLibrariesTask
import com.kaylakautai.buildlogic.OpenSourceCoordinatesTransformer
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

plugins {
    alias(libs.plugins.pikau.android.application.compose)
    alias(libs.plugins.pikau.hilt.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kaylakautai.pikau"

    defaultConfig {
        applicationId = "com.kaylakautai.pikau"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization { enable = false }
        }
    }
}

extensions.getByType<ApplicationAndroidComponentsExtension>().onVariants { variant ->
    val coordinates = variant.runtimeConfiguration.incoming.resolutionResult.rootComponent.map(
        OpenSourceCoordinatesTransformer(),
    )
    val generateLibraries = tasks.register<GenerateOpenSourceLibrariesTask>(
        "generate${variant.name.replaceFirstChar { it.uppercase() }}OpenSourceLibraries",
    ) {
        this.coordinates.set(coordinates)
        gradleUserHome.set(gradle.gradleUserHomeDir)
    }
    variant.sources.assets?.addGeneratedSourceDirectory(
        generateLibraries,
        GenerateOpenSourceLibrariesTask::outputDirectory,
    )
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.nav3)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":feature:home"))
    implementation(project(":feature:trips"))
    implementation(project(":feature:gear"))
    implementation(project(":feature:profile"))
}
