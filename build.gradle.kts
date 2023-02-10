import nl.vv32.rcon.Rcon
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
}

buildscript {
    dependencies {
        classpath("nl.vv32.rcon:rcon:1.2.0")
    }
}

group = "com.github.devlaq.toolhance"
version = "1.0-SNAPSHOT"

val testServerDir: String by project

val useRcon: String by project
val rconAddress: String? by project
val rconPassword: String? by project
val preCopyCommands: String? by project
val postCopyCommands: String? by project

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation(kotlin("test"))

    @Suppress("VulnerableLibrariesLocal")
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.github.monun:kommand-api:3.0.0")

    implementation("net.axay:kspigot:1.19.1")
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

    val useRcon = useRcon.toBoolean()


    if(!fatJar.archiveFile.get().asFile.exists()) return@task
    var rcon: Rcon? = null
    if(useRcon && rconAddress != null) {
        try {
            println("[RCON] Init...")
            rcon = Rcon.open(rconAddress!!.substringBeforeLast(":"), rconAddress!!.substringAfterLast(":").toInt())
            rcon.authenticate(rconPassword)
        } catch (e: Exception) {
            System.err.println("[RCON] Failed to connect to the server: ${e.localizedMessage}")
        }
    }
    if(useRcon && preCopyCommands != null && rcon != null) {
        val commands = preCopyCommands!!.split(";")
        println("[RCON] Running pre-copy commands: [${commands.joinToString(", ")}]")
        commands.forEach { rcon.sendCommand(it) }
    }
    val destFile = File("$testServerDir/plugins/Toolhance-TEST.jar")
    if(useRcon && postCopyCommands != null && rcon != null) {
        val commands = postCopyCommands!!.split(";")
        println("[RCON] Running post-copy commands: [${commands.joinToString(", ")}]")
        commands.forEach { rcon.sendCommand(it) }
    }
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


