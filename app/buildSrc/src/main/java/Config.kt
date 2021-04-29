import org.gradle.api.JavaVersion

object Config {
    const val applicationId = "dev.pimentel.counters"

    object Versions {
        private const val major = 0
        private const val minor = 1
        private const val patch = 0

        const val name = "$major.$minor.$patch"
        const val code = major * 10000 + minor * 100 + patch

        const val compileSdk = 30
        const val buildTools = "30.0.3"
        const val minSdk = 23
        const val targetSdk = 30

        object Compile {
            val sourceCompatibility = JavaVersion.VERSION_1_8
            val targetCompatibility = JavaVersion.VERSION_1_8
        }

        object Kotlin {
            const val jvmTarget = "1.8"
        }
    }

    object Projects {
        const val presentation = ":presentation"
        const val domain = ":domain"
        const val data = ":data"
    }
}
