import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0" // format: ./gradlew ktlintFormat
    id("org.openapi.generator") version "6.2.1"

    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "dev.pritam"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.openapitools:openapi-generator-gradle-plugin:5.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
val serverPath = "$rootDir/server-stub"

val generateServerStub by tasks.registering(GenerateTask::class) {
    inputSpec.set("$rootDir/spec.yml") // /brahma/api/spec.yml
    outputDir.set(serverPath)

    generatorName.set("kotlin-spring")
    apiPackage.set("org.pritam.blocking.api")
    modelPackage.set("org.pritam.blocking.model")
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "useTags" to "true",
            "interfaceOnly" to "true",
            "gradleBuildFile" to "false",
            "exceptionHandler" to "false",
            "enumPropertyNaming" to "UPPERCASE"
        )
    )
}
