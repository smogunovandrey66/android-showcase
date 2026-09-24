plugins {
    alias(libs.plugins.showcase.android.library)
    alias(libs.plugins.showcase.hilt)
}

dependencies {
    api(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.network)

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.ktx)
    ksp(libs.androidx.hilt.compiler)

    testImplementation(projects.core.testing)
    testImplementation(libs.mockk)
}
