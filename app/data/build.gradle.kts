plugins {
    id("com.android.library")
    kotlin("android")
    id("de.mannodermaus.android-junit5")
}

android {
    compileSdkVersion(Config.Versions.compileSdk)
    buildToolsVersion = Config.Versions.buildTools

    defaultConfig {
        minSdkVersion(Config.Versions.minSdk)
        targetSdkVersion(Config.Versions.targetSdk)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = Config.Versions.Compile.sourceCompatibility
        targetCompatibility = Config.Versions.Compile.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = Config.Versions.Kotlin.jvmTarget
    }
}

dependencies {
    implementation(project(Config.Projects.domain))

    testImplementation(Libs.Test.junitAPI)
    testRuntimeOnly(Libs.Test.junitEngine)
}
