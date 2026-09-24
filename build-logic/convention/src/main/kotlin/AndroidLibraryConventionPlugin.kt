import com.android.build.api.dsl.LibraryExtension
import com.smogunov.showcase.buildlogic.AndroidSdk
import com.smogunov.showcase.buildlogic.configureKotlinAndroid
import com.smogunov.showcase.buildlogic.library
import com.smogunov.showcase.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.targetSdk = AndroidSdk.TARGET
                lint.targetSdk = AndroidSdk.TARGET
                // Module namespace is derived from its Gradle path, e.g. :core:data -> ...core.data
                namespace = "com.smogunov.showcase" + path.replace(':', '.').replace('-', '_')
            }

            dependencies {
                add("testImplementation", libs.library("junit4"))
                add("testImplementation", libs.library("kotlin-test"))
            }
        }
    }
}
