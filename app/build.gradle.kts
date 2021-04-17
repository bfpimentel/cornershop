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
    }
}

allprojects {
    repositories {
        google()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        jcenter()
    }
}

tasks.create("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
