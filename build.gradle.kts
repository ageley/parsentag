plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "com.parsentag"
version = "1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

val telegrambots: String by project
val aws: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.telegram:telegrambots-springboot-longpolling-starter:$telegrambots")
    implementation("org.telegram:telegrambots-client:$telegrambots")
    implementation("software.amazon.awssdk:textract:$aws")
    implementation("software.amazon.awssdk:s3:$aws")
    implementation("com.zaxxer:HikariCP")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showExceptions = true
    testLogging.events("PASSED", "SKIPPED", "FAILED", "STANDARD_OUT", "STANDARD_ERROR")
    reports {
        html.required.set(true)
    }
}

tasks.bootJar {
    archiveFileName.set("parsentag.jar")
    exclude(".env")
    exclude("*parsentag-dev*")
}
