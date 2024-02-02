plugins {
    kotlin("jvm") version "1.9.21"
}

group = "org.holoeasy"
version = "3.1.1"

repositories {
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}