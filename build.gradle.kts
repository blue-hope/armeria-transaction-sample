import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.6"
    id("com.google.protobuf") version "0.8.18"
    kotlin("jvm") version "1.6.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val springBootVersion = "2.6.6"
val armeriaVersion = "1.21.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:$springBootVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.6")

    implementation("com.linecorp.armeria:armeria-spring-boot2-webflux-starter:$armeriaVersion")
    implementation("com.linecorp.armeria:armeria-grpc:$armeriaVersion")
    implementation("com.linecorp.armeria:armeria-kotlin:$armeriaVersion")
    implementation("io.grpc:grpc-stub:1.46.0")
    implementation("io.grpc:grpc-kotlin-stub:1.2.1")
    implementation("io.grpc:grpc-protobuf:1.46.0")
    implementation("com.google.protobuf:protobuf-kotlin:3.20.1")

    implementation("dev.miku:r2dbc-mysql:0.8.2.RELEASE")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    dependsOn("generateProto")
    enabled = true
    from("$buildDir/generated")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.20.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.46.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.2.1:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }

            it.generateDescriptorSet = true
            it.descriptorSetOptions.includeSourceInfo = true
            it.descriptorSetOptions.includeImports = true
            it.descriptorSetOptions.path =
                "${buildDir}/resources/main/META-INF/armeria/grpc/service-name.dsc"
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs(
                "src/main/kotlin",
                "build/generated/source/proto/main/grpc",
                "build/generated/source/proto/main/grpckt",
                "build/generated/source/proto/main/java",
                "build/generated/source/proto/main/kotlin",
            )
        }
    }
}
