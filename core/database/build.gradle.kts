plugins {
    alias(libs.plugins.pikau.android.library)
    alias(libs.plugins.pikau.hilt.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kaylakautai.pikau.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.androidx.room.compiler)
}