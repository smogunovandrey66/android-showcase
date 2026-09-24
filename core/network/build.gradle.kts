plugins {
    alias(libs.plugins.showcase.android.library)
    alias(libs.plugins.showcase.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://rickandmortyapi.com/api/\"")
    }
}

dependencies {
    api(libs.kotlinx.serialization.json)
    api(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
}
