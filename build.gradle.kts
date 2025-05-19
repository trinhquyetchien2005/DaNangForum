plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}
allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
	annotation("jakarta.transaction.Transactional")
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	google()
	maven { url = uri("https://jitpack.io") }
}

dependencies {
	// Spring basics
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	// Kotlin & Utils
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

	// Database
	implementation("mysql:mysql-connector-java:8.0.29")
	implementation("org.flywaydb:flyway-core")
	implementation("org.projectlombok:lombok")

	// OAuth2
	implementation("org.springframework.security:spring-security-oauth2-client")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Redis & Google API
	implementation("redis.clients:jedis:4.2.3")
	implementation("com.google.api-client:google-api-client:2.0.0")

	// WebSocket + STOMP (Android và Web)
	implementation("org.webjars:sockjs-client:1.5.1")
	implementation("org.webjars:stomp-websocket:2.3.4")
	implementation("org.java-websocket:Java-WebSocket:1.5.3")
	implementation("com.squareup.okhttp3:okhttp:4.10.0")
	implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")


	// RxJava bắt buộc cho Android STOMP
	implementation("io.reactivex.rxjava2:rxjava:2.2.21")
	implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

	// OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	// Devtools & testing
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


