plugins {
    kotlin("jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"

}

group = "org.holoeasy"
version = "3.1.1"


repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenCentral()
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo.
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("org.jetbrains:annotations:24.1.0")
}


tasks {
    shadowJar {
        relocate( "kotlin", "org.holoeasy.shaded.kotlin")
    }
}

// include java
configure<SourceSetContainer> {
    named("main") {
        java.srcDir("src/main/kotlin")
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}