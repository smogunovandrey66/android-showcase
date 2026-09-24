plugins {
    alias(libs.plugins.showcase.android.library)
    alias(libs.plugins.showcase.android.room)
    alias(libs.plugins.showcase.hilt)
}

dependencies {
    api(projects.core.model)
    api(libs.androidx.room.ktx)
    api(libs.androidx.room.paging)
    api(libs.androidx.room.runtime)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.turbine)
}
