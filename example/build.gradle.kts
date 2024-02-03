plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven(url = uri("https://repo.dmulloy2.net/repository/public/"))
    maven(url = uri("https://jitpack.io"))
    maven(url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"))
}


group = "org.holoeasy"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.github.GeorgeV220:Hologram-Lib:33cba59492")
}
