import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Libs.Gradle.kotlin)
        classpath(Libs.Gradle.junit5)
        classpath(Libs.Gradle.android)
        classpath(Libs.Gradle.hilt)
        classpath(Libs.Gradle.navigation)
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.7.4"
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

apply(from = "jacoco.gradle")

tasks.create("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}

subprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            languageVersion = "1.4"
            jvmTarget = Config.Versions.Kotlin.jvmTarget
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.FlowPreview"
        }
    }
}
