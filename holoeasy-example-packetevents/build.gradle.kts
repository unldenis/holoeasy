/**
 * Hologram-Lib - An asynchronous, high-performance Minecraft Hologram library
 * for servers running versions 1.8 to 1.18.
 *
 * Copyright (C) 2023 unldenis <https://github.com/unldenis>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

plugins {
    id("buildlogic.java-conventions")
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    implementation(libs.org.jetbrains.kotlin.kotlin.stdlib)
    implementation(project(":holoeasy-core")) {
        // Set the transitive dependency to false to avoid shading HoloEasy dependencies
        isTransitive = false
    }
    implementation(project(":holoeasy-folia")) {
        // Set the transitive dependency to false to avoid shading HoloEasy dependencies
        isTransitive = false
    }
    implementation(libs.com.github.retrooper.packetevents.spigot)
    compileOnly(libs.org.spigotmc.spigot.api)
}

// Folia requires Java 17
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

description = "holoeasy-example-packetevents"

tasks {
    shadowJar {
        archiveClassifier.set("shadow")
        archiveVersion.set("${project.version}")

        // It is important to relocate org.holoeasy and io.github.retrooper.packetevents
        // to avoid conflicts with other plugins
        //relocate("org.holoeasy", "<your package name>.holoeasy")
        relocate("io.github.retrooper.packetevents", "org.holoeasy.plugin.packetevents")
        relocate("com.github.retrooper.packetevents", "org.holoeasy.plugin.packetevents")
        relocate("net.kyori", "org.holoeasy.plugin.kyori")
    }
}

