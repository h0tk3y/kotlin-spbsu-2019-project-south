import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
}

group = "south"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation ("mysql","mysql-connector-java", "5.1.40")
    compile("org.slf4j", "slf4j-simple", "1.7.25")
    compile("org.jetbrains.exposed:exposed:0.16.1")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    implementation("org.slf4j","slf4j-api","1.7.5")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.4.2-rc1")
    implementation("io.ktor:ktor-server-netty:1.2.4")
    implementation("io.ktor:ktor-websockets:1.2.4")
    implementation("io.ktor:ktor-client-websockets:1.2.4")
    implementation("io.ktor:ktor-client-websockets:1.2.4")
    implementation("io.ktor:ktor-client-cio:1.2.4")
    implementation("io.ktor:ktor-client-js:1.2.4")
    implementation("io.ktor:ktor-client-okhttp:1.2.4")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}