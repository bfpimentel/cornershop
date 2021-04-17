plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("de.mannodermaus.android-junit5")
}

android {
    compileSdkVersion(Config.Versions.compileSdk)
    buildToolsVersion = Config.Versions.buildTools

    defaultConfig {
        applicationId = Config.applicationId
        minSdkVersion(Config.Versions.minSdk)
        targetSdkVersion(Config.Versions.targetSdk)
        versionCode = Config.Versions.code
        versionName = Config.Versions.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
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
    implementation(project(Config.Projects.data))

    implementation(Libs.Android.materialDesign)
    implementation(Libs.Android.fragment)
    implementation(Libs.Android.startup)
    implementation(Libs.Android.lifecycleCommon)

    implementation(Libs.Kotlin.coroutinesCore)

    implementation(Libs.Navigation.fragment)
    implementation(Libs.Navigation.ui)

    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)

    testImplementation(Libs.Test.coroutinesCore)
    testImplementation(Libs.Test.mockk)
    testImplementation(Libs.Test.junitAPI)
    testRuntimeOnly(Libs.Test.junitEngine)
}
