plugins {
    id("buildlogic.java-conventions")
    `kotlin-dsl`
}

repositories {
    flatDir {
        dirs("../libs")
    }
}

dependencies {
    compileOnly(libs.org.spigotmc.spigot.api)
    compileOnly(libs.com.github.retrooper.packetevents.spigot)
    compileOnly(libs.adventure.text.serializer.gson)
}

tasks.register("printVersion") {
    doLast {
        println(project.version)
    }
}


description = "holoeasy-core"