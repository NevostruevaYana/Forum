val ktor_version = "1.6.0"
val kotlin_version = "1.6.0"
val logback_version = "1.2.3"
val jackson_version = "2.6.0-rc1"

plugins {
    application
    kotlin("jvm") version "1.6.0"
    id("org.flywaydb.flyway") version "8.5.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
}

group = "com.monkeys"
application {
    mainClass.set("com.monkeys.ApplicationKt")
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-gson:$ktor_version")

    implementation("commons-codec:commons-codec:1.5")


    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("org.postgresql:postgresql:42.3.1")
    implementation("org.ktorm:ktorm-core:3.3.0")
    implementation("org.flywaydb:flyway-core:8.5.2")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("com.fasterxml.jackson.core:jackson-core:$jackson_version")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jackson_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.monkeys.ApplicationKt"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations.compileClasspath.get().forEach {
        from(if (it.isDirectory) it else zipTree(it))
    }
}
