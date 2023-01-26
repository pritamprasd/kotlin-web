# Notes on tools used.
## Gradle:
### Commands
```sh
./gradlew build     # build project
./gradlew tasks     # see available tasks

./gradlew clean
./gradlew compileJava
./gradlew processResources
./gradlew jar
./gradlew test

# format with ktlint, id("org.jlleitschuh.gradle.ktlint") 
./gradlew ktlintFormat  

# Openapi
./gradlew openApiGenerate
./gradlew openApiGenerators
./gradlew openApiMeta
./gradlew openApiValidate
```

### plugins
additional build functionality

### repositories
where to get dependencies

### Gotchas
- No Manifest found
```kotlin
jar{
    manifest{
        attributes('Main-Class': 'dev.pritam.code.MainClass')
    }
}
```
- java plugin task graph:
to view task dependencies: `./gradlew <taskname> --dry-run`
```
:build
+--- :assemble
    \--- :jar
        \--- :classes
            +--- :compileJava
            \--- processResources
\--- :check
    \--- :test
        +--- :classes
            +--- :compileJava
            \--- processResources
        \--- :testClasses
            +--- :compileTestJava
                \--- :classes
                    +--- :compileJava
                    \--- processResources
            \--- processTestResources
```


