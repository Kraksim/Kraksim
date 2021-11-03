import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.spring") version "1.5.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    kotlin("plugin.jpa") version "1.5.0"
    kotlin("kapt") version "1.5.10"
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("junit:junit:4.13.1")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("de.vandermeer:asciitable:0.3.2")
    testImplementation("org.testcontainers:postgresql:1.16.0")
    testImplementation("org.testcontainers:junit-jupiter:1.16.0")
    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.11")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
    }
    useBuildCache = false
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
