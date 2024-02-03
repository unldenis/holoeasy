plugins {
    java
    kotlin("jvm") version "1.9.21"
    `maven-publish`
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

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId =  "org.holoeasy"
            artifactId = "holoeasy"
            version = "3.1.1"

            from(components["java"])
        }
    }
}

kotlin {
    jvmToolchain(8)
}