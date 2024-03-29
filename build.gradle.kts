import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
    Plugin needed to compile Kotlin -> JVM bytecode.
    "version ..." != jvmTarget
 */
plugins {
    kotlin("jvm") version "1.5.31"
}

group = "me.alekseibingham"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

// Compile to Java 8 bytecode
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}