import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

group = "dev.loosername"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "dev.loosername.MainKt"
}
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
dependencies {
    implementation("io.javalin:javalin:3.11.0")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    implementation("com.github.PhilJay:JWT:1.1.5")
    implementation("com.github.kmehrunes:javalin-jwt:v0.2")
    testImplementation(kotlin("test-junit"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}