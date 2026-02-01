import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  val kotlinVersion = "2.3.0"
  val loomVersion = "1.15-SNAPSHOT"
  val ktfmtPluginVersion = "0.25.0"
  kotlin("jvm") version kotlinVersion
  kotlin("plugin.serialization") version kotlinVersion
  id("net.fabricmc.fabric-loom-remap") version loomVersion
  id("com.ncorti.ktfmt.gradle") version ktfmtPluginVersion
}

val version = "0.1.0"
val group = "net.yqloss"

val minecraftVersion = "1.21.10"
val fabricLoaderVersion = "0.18.4"
val fabricApiVersion = "0.138.4+1.21.10"
val fabricLanguageKotlinVersion = "1.13.8+kotlin.2.3.0"

val jcefVersion = "141.0.10"
val jcefApiVersion = "jcef-2caef5a+cef-141.0.10+g1d65b0d+chromium-141.0.7390.123"

base { archivesName = "yqloss-client-resumed-$version-fabric-$minecraftVersion" }

repositories {
  maven {
    name = "ParchmentMC"
    url = uri("https://maven.parchmentmc.org")
  }
}

dependencies {
  minecraft("com.mojang:minecraft:$minecraftVersion")

  mappings(
      loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.21.10:2025.10.12@zip")
      })

  modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
  modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")
  modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

  implementation("me.friwi:jcefmaven:$jcefVersion")
  include("me.friwi:jcefmaven:$jcefVersion")
  include("me.friwi:jcef-api:$jcefApiVersion")
}

val buildWeb by
    tasks.registering(Exec::class) {
      group = "build"
      workingDir = file("web")
      commandLine("pnpm", "run", "build")
    }

tasks.processResources {
  dependsOn(buildWeb)

  inputs.property("version", version)
  filesMatching("fabric.mod.json") { expand("version" to version) }
  from(layout.projectDirectory.dir("web/dist")) { into("web") }
}

tasks.withType<JavaCompile>().configureEach { options.release = 21 }

java {
  withSourcesJar()

  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21

  toolchain { languageVersion = JavaLanguageVersion.of(21) }
}

kotlin {
  jvmToolchain(21)
  compilerOptions {
    jvmTarget = JvmTarget.JVM_21
    freeCompilerArgs.addAll("-Xcontext-parameters")
    optIn.addAll("kotlin.contracts.ExperimentalContracts", "kotlin.uuid.ExperimentalUuidApi")
  }
}
