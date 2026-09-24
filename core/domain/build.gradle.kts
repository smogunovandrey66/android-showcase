plugins {
    alias(libs.plugins.showcase.jvm.library)
}

dependencies {
    api(projects.core.model)
    api(libs.androidx.paging.common)
    api(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)

    testImplementation(projects.core.testing)
}
