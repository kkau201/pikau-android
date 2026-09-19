plugins {
    alias(libs.plugins.pikau.android.library)
    alias(libs.plugins.pikau.hilt.library)
}

android {
    namespace = "com.kaylakautai.data"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    implementation(project(":core:database"))
    implementation(project(":core:model"))
}
