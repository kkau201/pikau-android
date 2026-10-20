plugins {
    alias(libs.plugins.pikau.android.library.compose)
}

android {
    namespace = "com.kaylakautai.pikau.ui"
}

dependencies {
    implementation(libs.lottie.compose)
    testImplementation(libs.junit)
}