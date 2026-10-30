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
    implementation(project(":feature:trips"))
    implementation(project(":feature:gear"))
    implementation(project(":feature:profile"))
}
