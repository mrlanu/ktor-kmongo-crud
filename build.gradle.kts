val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.30"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.30"
}

group = "io.lanu"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.litote.kmongo:kmongo-coroutine:4.2.8")
    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:3.1.2")
// SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j:3.1.2")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}
