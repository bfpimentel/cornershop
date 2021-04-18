plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    testImplementation(Libs.Test.coroutinesCore)
    testImplementation(Libs.Test.mockk)
    testImplementation(Libs.Test.junitAPI)
    testRuntimeOnly(Libs.Test.junitEngine)
}
