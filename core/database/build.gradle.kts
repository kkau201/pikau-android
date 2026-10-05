plugins {
    alias(libs.plugins.pikau.android.library)
    alias(libs.plugins.pikau.hilt.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kaylakautai.pikau.core.database"
    testFixtures.enable = true
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.robolectric)

    testFixturesImplementation(project(":core:model"))
}