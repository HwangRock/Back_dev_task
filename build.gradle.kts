plugins {
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
	}
}


dependencies {
	// spring
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// security
	implementation("org.springframework.boot:spring-boot-starter-security")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.12.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

	implementation("org.springframework.boot:spring-boot-starter-validation")

	// JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

	// PostgreSQL
	runtimeOnly("org.postgresql:postgresql")

	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Feign client
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

	// 	resilience4j
	implementation("io.github.resilience4j:resilience4j-spring-boot3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
