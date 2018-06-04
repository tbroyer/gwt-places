import net.ltgt.gradle.apt.* // ktlint-disable no-wildcard-imports

plugins {
    id("local.java-library")
    id("local.maven-publish")
    id("net.ltgt.apt") version "0.15"
}
apply(from = "funcTest.gradle.kts")
apply(from = "gwtTest.gradle.kts")

base.archivesBaseName = "gwt-places-processor"

// src/testSupport/java will be compiled twiced: once with annotation processing, and once without
java.sourceSets {
    "test" {
        java {
            srcDir("src/testSupport/java")
        }
    }
}

// Do not run annotation processor, as we'll be testing it with compile-testing
val compileTestJava by tasks.getting(JavaCompile::class) {
    aptOptions.annotationProcessing = false
}

repositories {
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly("com.google.auto.service:auto-service:1.0-rc4")
    annotationProcessor("com.google.auto.service:auto-service:1.0-rc4")

    api("com.google.auto:auto-common:0.10")
    api("com.google.guava:guava:25.1-jre")
    implementation(project(":")) {
        isTransitive = false
    }
    implementation("com.squareup:javapoet:1.11.1")

    testImplementation("junit:junit:4.12")
    testImplementation("com.google.testing.compile:compile-testing:0.15")
    testImplementation("org.mockito:mockito-core:2.18.3")
}
