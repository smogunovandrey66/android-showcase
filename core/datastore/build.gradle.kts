plugins {
    alias(libs.plugins.showcase.android.library)
    alias(libs.plugins.showcase.hilt)
}

dependencies {
    api(projects.core.model)
    api(libs.androidx.dataStore.preferences)
    implementation(projects.core.common)

    testImplementation(libs.kotlinx.coroutines.test)
}
