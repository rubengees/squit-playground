import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("com.github.johnrengelman.shadow") version "5.0.0"
    id("de.smartsquare.squit") version "2.5.0"
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut:micronaut-runtime")

    runtime("ch.qos.logback:logback-classic:1.2.3")
}

application {
    mainClassName = "com.rubengees.playground.Application"
}

configure<DependencyManagementExtension> {
    imports {
        mavenBom("io.micronaut:micronaut-bom:1.0.5")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.javaParameters = true
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
}

task("squitCopyResults", type = Copy::class) {
    dependsOn("squitTest")

    from(File(buildDir, "squit/reports/html"))
    into(File(buildDir, "resources/main/public"))
    rename("main.html", "index.html")
}
