plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("war")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //CACHE
    implementation("org.springframework.boot:spring-boot-starter-cache")
    //VALIDACIONES
    implementation("org.springframework.boot:spring-boot-starter-validation")
    //JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //XML_JSON
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    //POSTGRES
    implementation ("org.postgresql:postgresql:42.7.2")
    //H2
    runtimeOnly("com.h2database:h2")
    //WEBSOCKET
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    //SWAGGER
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    //MONGODB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    //THYMELEAF
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    //SECURITY
    implementation("org.springframework.boot:spring-boot-starter-security")
    //JWT
    implementation("com.auth0:java-jwt:4.4.0")
    //MAIL
    implementation("org.springframework.boot:spring-boot-starter-mail")
    //ENV
    implementation("io.github.cdimascio:dotenv-java:3.0.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Copy>("copyFrontend") {
    //dependsOn(":AngOposchool:build")
    // Debo crear una tarea build que ejecute el comando ng build para que se cree la carpeta dist automaticamente.
    // Mientras tanfo ejecutamos manualmente el comando antes de generar el war
    from("AngOposchool/dist/ang-oposchool/browser")
    into("src/main/resources/static")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootWar>("bootWar") {
    dependsOn("copyFrontend")
    mainClass.set("com.example.tfgoposchool.TfgOposchoolApplication")
}

tasks.named("processResources") {
    dependsOn("copyFrontend")
}
