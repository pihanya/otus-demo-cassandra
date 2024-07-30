plugins {
    kotlin("jvm") version "1.9.25"
}

group = "ru.otus"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.cassandra:java-driver-core:4.18.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.25")

    testImplementation("org.testcontainers:cassandra:1.20.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
