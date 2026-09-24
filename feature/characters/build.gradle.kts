plugins {
    alias(libs.plugins.showcase.android.feature)
}

dependencies {
    implementation(libs.androidx.paging.compose)

    testImplementation(libs.androidx.paging.testing)
}
