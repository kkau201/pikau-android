plugins {
    alias(libs.plugins.pikau.android.library)
    alias(libs.plugins.pikau.hilt.library)
}

android {
    namespace = "com.kaylakautai.data"
    testFixtures.enable = true
}

dependencies {
    testImplementation(testFixtures(project(":core:database")))
    testImplementation(testFixtures(project(":core:model")))
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.robolectric)

    testFixturesImplementation(project(":core:model"))
    testFixturesImplementation(libs.kotlinx.coroutines)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    api(project(":core:database"))
    implementation(project(":core:model"))
}
