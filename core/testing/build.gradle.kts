plugins {
    alias(libs.plugins.showcase.jvm.library)
}

dependencies {
    api(projects.core.domain)
    api(projects.core.model)
    api(libs.junit4)
    api(libs.kotlin.test.junit)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)
}
