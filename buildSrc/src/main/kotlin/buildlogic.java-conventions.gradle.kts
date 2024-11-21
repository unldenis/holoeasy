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
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

    maven {
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }

    maven {
        url = uri("https://repo.codemc.io/repository/maven-snapshots/")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
}

group = "org.holoeasy"
version = "4.3.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}