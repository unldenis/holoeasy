import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
}

group = "org.holoeasy"
version = "3.1.1"

apply(from = "$rootDir/gradle/publish.gradle")

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:24.0.1")
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceSets {
            getByName("main").java.srcDirs("src/main/java")
            getByName("test").java.srcDirs("src/test/java")
        }
    }

    configure<KotlinJvmProjectExtension> {
        sourceSets {
            getByName("main").kotlin.srcDirs("src/main/kotlin")
            getByName("test").kotlin.srcDirs("src/test/kotlin")
        }

        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
}

dependencies {
    implementation(project(":core"))
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        relocate("kotlin.", "org.holoeasy.kotlin.")
        relocate("org.intellij.lang.annotations", "org.holoeasy.annotations")
        relocate("org.jetbrains.annotations", "org.holoeasy.annotations")
    }

    named("build") {
        dependsOn("shadowJar")
    }

    named("publishToMavenLocal") {
        dependsOn("jar", "shadowJar")
    }

    named("publishShadowPublicationToMavenLocal") {
        dependsOn("jar", "shadowJar")
    }

    named("publish") {
        dependsOn("shadowJar")
    }

}
