plugins {
    alias(libs.plugins.showcase.android.library)
    alias(libs.plugins.showcase.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
