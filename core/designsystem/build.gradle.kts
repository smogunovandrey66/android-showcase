plugins {
    alias(libs.plugins.showcase.android.library)
    alias(libs.plugins.showcase.android.library.compose)
}

dependencies {
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.iconsCore)
}
