plugins {
    alias(libs.plugins.pikau.android.library)
}

android {
    namespace = "com.kaylakautai.pikau.core.database"
}

dependencies {
    implementation(project(":core:model"))
}