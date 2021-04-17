plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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
        val apiUrlResName = "api_url"
        val apiUrl = "http://localhost:3000/api/"

        getByName("release") {
            resValue("string", apiUrlResName, apiUrl)
        }
        getByName("debug") {
            resValue("string", apiUrlResName, apiUrl)
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

    implementation(Libs.Networking.moshi)
    implementation(Libs.Networking.retrofit)
    implementation(Libs.Networking.retrofitMoshi)

    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)

    testImplementation(Libs.Test.junitAPI)
    testRuntimeOnly(Libs.Test.junitEngine)
}
