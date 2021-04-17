import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
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
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        jcenter()
    }
}

apply(from = "jacoco.gradle")

tasks.create("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        languageVersion = "1.4"
    }
}
