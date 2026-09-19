plugins {
    alias(libs.plugins.pikau.android.library)
}

android {
    namespace = "com.kaylakautai.data"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
