plugins {
    alias(libs.plugins.pikau.jvm.library)
    alias(libs.plugins.kotlin.serialization)
    id("java-test-fixtures")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
