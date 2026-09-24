import androidx.room.gradle.RoomExtension
import com.smogunov.showcase.buildlogic.library
import com.smogunov.showcase.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "androidx.room")
            apply(plugin = "com.google.devtools.ksp")

            extensions.configure<RoomExtension> {
                // Exported schemas are committed to VCS and used for migration tests.
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                add("implementation", libs.library("androidx-room-runtime"))
                add("implementation", libs.library("androidx-room-ktx"))
                add("ksp", libs.library("androidx-room-compiler"))
            }
        }
    }
}
