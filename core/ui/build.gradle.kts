plugins {
    alias(libs.plugins.pikau.android.library.compose)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.kaylakautai.pikau.ui"

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

roborazzi {
    outputDir.set(file("src/test/screenshots"))
}

dependencies {
    implementation(libs.lottie.compose)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
}