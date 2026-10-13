plugins {
    alias(libs.plugins.pikau.android.library.compose)
    alias(libs.plugins.pikau.hilt.library)
}

android {
    namespace = "com.kaylakautai.trips"
    resourcePrefix = "trips_"
    testOptions { unitTests.isReturnDefaultValues = true }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    testImplementation(testFixtures(project(":core:model")))
    testImplementation(libs.junit)

}