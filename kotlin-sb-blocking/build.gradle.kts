
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
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-logging")
    }
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.openapitools:openapi-generator-gradle-plugin:5.0.0")
    implementation("javax.validation:validation-api:2.0.1.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val serverPath = "$rootDir/src/main/kotlin/dev/pritam/blocking/generated"

val preGenerateCleanup by tasks.registering(Delete::class) {
    delete = setOf(
        fileTree(serverPath),
    )
}
val generateServerStub by tasks.registering(GenerateTask::class) {
    dependsOn(preGenerateCleanup)
    inputSpec.set("$rootDir/spec.yml")
    outputDir.set("$rootDir")

    generatorName.set("kotlin-spring")
    apiPackage.set("dev.pritam.blocking.generated.api")
    modelPackage.set("dev.pritam.blocking.generated.model")
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "useTags" to "true",
            "interfaceOnly" to "true",
            "gradleBuildFile" to "false",
            "exceptionHandler" to "false",
            "enumPropertyNaming" to "UPPERCASE",
        )
    )
}
val postGenerateCleanup by tasks.registering(Delete::class) {
    dependsOn(generateServerStub)
    delete = setOf(
        fileTree("$rootDir/src/main/kotlin/org/"),
        file("$rootDir/.openapi-generator-ignore"),
        file("$rootDir/pom.xml"),
        file("$rootDir/README.md"),
        file("$serverPath/api/ApiUtil.kt")
    )
}

ktlint {
    filter {
        exclude { e -> e.file.path.contains("generated/") }
    }
}

tasks.ktlintFormat {}

tasks.named<KotlinCompile>("compileKotlin") {
    dependsOn(postGenerateCleanup)
    dependsOn(tasks.ktlintFormat)
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
