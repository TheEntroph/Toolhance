import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
}

group = "com.github.devlaq.toolhance"
version = "1.0-SNAPSHOT"

val testServerDir: String by project

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation(kotlin("test"))

    @Suppress("VulnerableLibrariesLocal")
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.github.monun:kommand-api:3.0.0")
    implementation("io.github.monun:invfx-api:3.2.0")
}

task("fatJar", type = Jar::class) {
    dependsOn(tasks.jar)
    archiveBaseName.set("${project.name}-FAT")
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

task("testJar") {
    val fatJar = tasks.getByName("fatJar") as Jar
    dependsOn(fatJar)
    if(!fatJar.archiveFile.get().asFile.exists()) return@task
    val destFile = File("$testServerDir/plugins/Toolhance-TEST.jar")
    doLast {
        tasks.jar.get().archiveFile.get().asFile.copyTo(destFile, overwrite = true)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}


