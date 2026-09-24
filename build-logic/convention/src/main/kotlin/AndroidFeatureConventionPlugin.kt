import com.smogunov.showcase.buildlogic.library
import com.smogunov.showcase.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * A feature module = Compose UI + ViewModel + type-safe navigation entry point.
 * Features never depend on each other; they are wired together in :app.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "showcase.android.library")
            apply(plugin = "showcase.android.library.compose")
            apply(plugin = "showcase.hilt")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                add("implementation", project(":core:common"))
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":core:domain"))
                add("implementation", project(":core:model"))
                add("implementation", project(":core:ui"))

                add("implementation", libs.library("androidx-hilt-navigation-compose"))
                add("implementation", libs.library("androidx-lifecycle-runtime-compose"))
                add("implementation", libs.library("androidx-lifecycle-viewmodel-compose"))
                add("implementation", libs.library("androidx-navigation-compose"))
                add("implementation", libs.library("kotlinx-serialization-json"))

                add("testImplementation", project(":core:testing"))
                add("testImplementation", libs.library("androidx-compose-ui-test-junit4"))
                add("testImplementation", libs.library("androidx-test-ext-junit"))
                add("testImplementation", libs.library("robolectric"))
                add("debugImplementation", libs.library("androidx-compose-ui-test-manifest"))
            }
        }
    }
}
