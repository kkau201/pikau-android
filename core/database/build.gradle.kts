plugins {
    alias(libs.plugins.pikau.android.library)
    alias(libs.plugins.pikau.hilt.library)
}

android {
    namespace = "com.kaylakautai.pikau.core.database"
}

dependencies {
    implementation(project(":core:model"))
}