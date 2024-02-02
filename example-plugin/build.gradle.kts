plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"

}

group = "org.holoeasy"
version = "3.1.0"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")

    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo.

    implementation(project(":api"))

    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")

}

tasks {
    shadowJar {
        relocate( "kotlin", "org.holoeasy.plugin.kotlin")
        destinationDirectory = file("C:\\Users\\mehil\\OneDrive\\Desktop\\all\\spigot 1.8.8\\plugins")
    }
}

tasks.test {
    useJUnitPlatform()
}