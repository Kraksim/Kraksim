import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.spring") version "1.5.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
//    kotlin("plugin.jpa") version "1.5.0" todo uncomment when database will be necessary
}

group = "pl.edu.agh.cs"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa") todo uncomment when database will be necessary
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
//    runtimeOnly("org.postgresql:postgresql") todo uncomment when database will be necessary
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("de.vandermeer:asciitable:0.3.2")
}

tasks {
    bootJar {
        archiveFileName.set("kraksim.jar")
    }

    processResources {
        expand(project.properties)
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    disableCodeFormattingChecks("ktlintFormat", "ktlintCheck")
}

/**
 * Disable code formatting checks,
 * when e.x. `gradle build` we don't want to fail because of code format,
 * so we disable checks for all tasks except those specified in [except]
 */
fun disableCodeFormattingChecks(vararg except: String) {
    project.gradle.taskGraph.whenReady {
        val taskNames = project.gradle.startParameter.taskNames
        if (!taskNames.any { it in except }) {
            allTasks.filter {
                it.name.contains("ktlint", true)
            }.forEach {
                it.enabled = false
            }
        }
    }
}
