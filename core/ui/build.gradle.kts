plugins {
    alias(libs.plugins.showcase.android.library)
    alias(libs.plugins.showcase.android.library.compose)
}

dependencies {
    api(projects.core.designsystem)
    api(projects.core.model)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
