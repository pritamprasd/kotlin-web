
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property


plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
//    id("org.jlleitschuh.gradle.ktlint") version "10.1.0" // format: ./gradlew ktlintFormat
    id("org.openapi.generator") version "6.2.1"
    id("org.flywaydb.flyway") version "9.14.1"
    id("nu.studer.jooq") version "8.1"

    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "dev.pritam"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

flyway {
    url = "jdbc:postgresql://localhost:54320/postgres"
    user = System.getenv("POSTGRES_USER")
    password = System.getenv("POSTGRES_PASSWORD")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-logging")
    }
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.openapitools:openapi-generator-gradle-plugin:5.0.0")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    jooqGenerator("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

val generatedPath = "$rootDir/build/generated"
val serverPath = "$generatedPath/openapi"

sourceSets {
    main {
        java {
            srcDirs(
                "$generatedPath/openapi/src/main",
                "$generatedPath/jooq/main"
            )
        }
    }
}

jooq {
   configurations {
      create("main") {
          jooqConfiguration.apply {
              logging = Logging.WARN
              jdbc.apply {
                  driver = "org.postgresql.Driver"
                  url = "jdbc:postgresql://localhost:54320/postgres"
                  user = System.getenv("POSTGRES_USER")
                  password = System.getenv("POSTGRES_PASSWORD")
//                  properties.add(Property().apply {
//                      key = "ssl"
//                      value = "true"
//                  })
              }
              generator.apply {
                  name = "org.jooq.codegen.DefaultGenerator"
                  database.apply {
                      name = "org.jooq.meta.postgres.PostgresDatabase"
                      inputSchema = "public"
                      excludes = "flyway_schema_history"
//                      forcedTypes.addAll(listOf(
//                          ForcedType().apply {
//                              name = "varchar"
//                              includeExpression = ".*"
//                              includeTypes = "JSONB?"
//                          },
//                          ForcedType().apply {
//                              name = "varchar"
//                              includeExpression = ".*"
//                              includeTypes = "INET"
//                          }
//                      ))
                  }
                  generate.apply {
                      isDeprecated = false
                      isRecords = true
                      isImmutablePojos = true
                      isFluentSetters = true
                  }
                  target.apply {
                      packageName = "dev.pritam.blocking"
                      directory = "build/generated/jooq/main"
                  }
                  strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
              }
          }
      }
   }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val preGenerateCleanup by tasks.registering(Delete::class) {
    delete = setOf(
        fileTree(serverPath),
    )
}
val generateServerStub by tasks.registering(GenerateTask::class) {
    dependsOn(preGenerateCleanup)
    inputSpec.set("$rootDir/spec.yml")
    outputDir.set(serverPath)

    generatorName.set("kotlin-spring")
    apiPackage.set("dev.pritam.blocking.api")
    modelPackage.set("dev.pritam.blocking.model")
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
        fileTree("$serverPath/src/main/kotlin/org/"),
//        fileTree("$serverPath/.openapi-generator"),
        file("$serverPath/.openapi-generator-ignore"),
        file("$serverPath/pom.xml"),
        file("$serverPath/README.md"),
        file("$serverPath/src/main/kotlin/dev/pritam/blocking/api/ApiUtil.kt")
    )
}

//ktlint {
//    filter {
//        exclude { e -> e.file.path.contains("generated/") }
//    }
//}

//tasks.ktlintFormat {}

tasks.named<KotlinCompile>("compileKotlin") {
    dependsOn(postGenerateCleanup)
//    dependsOn(tasks.ktlintFormat)
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
