package com.smogunov.showcase.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Shared Android + Kotlin configuration for application and library modules.
 */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = AndroidSdk.COMPILE

        defaultConfig {
            minSdk = AndroidSdk.MIN
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        testOptions {
            unitTests {
                // Needed by Robolectric (Compose UI tests and Room tests run on the JVM).
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
            }
        }

        lint {
            abortOnError = true
        }
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions.configureShared()
    }
}

/**
 * Configuration for pure Kotlin/JVM modules (no Android dependencies).
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    extensions.configure<KotlinJvmProjectExtension> {
        compilerOptions.configureShared()
    }
}

private fun KotlinJvmCompilerOptions.configureShared() {
    jvmTarget.set(JvmTarget.JVM_17)
    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
