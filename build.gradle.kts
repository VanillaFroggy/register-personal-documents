plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.internship"
version = "1.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "com.internship.Application"
    }
}

application {
    mainClass.set("com.internship.Application")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework:spring-framework-bom:6.2.3"))
    implementation(platform("org.springframework.data:spring-data-bom:2024.1.3"))
    implementation(platform("org.springframework.security:spring-security-bom:6.4.3"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.security:spring-security-config")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")
    implementation("org.apache.tomcat.embed:tomcat-embed-core:11.0.4")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:11.0.4")
    implementation("org.slf4j:slf4j-api")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("org.aspectj:aspectjweaver:1.9.22.1")
    implementation("org.liquibase:liquibase-core:4.31.1")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.hibernate.orm:hibernate-core:6.6.7.Final")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("org.hibernate.validator:hibernate-validator:9.0.0.CR1")
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("org.thymeleaf:thymeleaf:3.1.3.RELEASE")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.3.RELEASE")
    implementation("org.mapstruct:mapstruct:1.6.3")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    compileOnly("org.projectlombok:lombok:1.18.36")
    runtimeOnly("org.aspectj:aspectjweaver:1.9.22.1")
    runtimeOnly("org.postgresql:postgresql:42.7.5")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}