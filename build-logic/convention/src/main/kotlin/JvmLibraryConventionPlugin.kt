import com.smogunov.showcase.buildlogic.configureKotlinJvm
import com.smogunov.showcase.buildlogic.library
import com.smogunov.showcase.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.jvm")
            configureKotlinJvm()

            dependencies {
                add("testImplementation", libs.library("junit4"))
                add("testImplementation", libs.library("kotlin-test-junit"))
            }
        }
    }
}
