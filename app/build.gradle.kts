plugins {
    alias(libs.plugins.showcase.android.application)
    alias(libs.plugins.showcase.android.application.compose)
    alias(libs.plugins.showcase.hilt)
}

android {
    namespace = "com.smogunov.showcase"

    defaultConfig {
        applicationId = "com.smogunov.showcase"
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // Signed with the debug key only so that the release build can be installed locally.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    lint {
        checkDependencies = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.feature.characters)
    implementation(projects.feature.details)
    implementation(projects.feature.favorites)
    implementation(projects.feature.settings)

    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.model)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.work.ktx)
    ksp(libs.androidx.hilt.compiler)
}
