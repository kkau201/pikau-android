plugins {
    alias(libs.plugins.pikau.android.library.compose)
    alias(libs.plugins.pikau.hilt.library)
}

android {
    namespace = "com.kaylakautai.trips"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    testImplementation(testFixtures(project(":core:model")))
    testImplementation(libs.junit)

}