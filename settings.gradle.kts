rootProject.name = "parsentag"

pluginManagement {
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val sonarlint: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("name.remal.sonarlint") version sonarlint
    }
}
