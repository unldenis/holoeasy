plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
}

group = "org.holoeasy"
version = "3.1.1"

repositories {
    mavenCentral()
    maven(url = uri("https://repo.dmulloy2.net/repository/public/"))
    maven(url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"))
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "MainKt"
        )
    }
}
