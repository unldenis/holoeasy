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
    `kotlin-dsl`
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    implementation(libs.org.jetbrains.kotlin.kotlin.stdlib)
    implementation(project(":holoeasy-core")) {
        // Set the transitive dependency to false to avoid shading HoloEasy dependencies
        isTransitive = false
    }
    compileOnly(libs.org.spigotmc.spigot.api)
}

description = "holoeasy-example-protocollib"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks {
    shadowJar {
        archiveClassifier.set("shadow")
        archiveVersion.set("${project.version}")

        // It is important to relocate org.holoeasy to avoid conflicts with other plugins
        //relocate("org.holoeasy", "<your package name>.holoeasy")
        relocate("net.kyori", "org.holoeasy.plugin.kyori")
    }
}