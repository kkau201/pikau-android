plugins {
    alias(libs.plugins.pikau.android.application.compose)
    alias(libs.plugins.pikau.hilt.library)
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    implementation(project(":core:ui"))
    implementation(project(":core:model"))
}
