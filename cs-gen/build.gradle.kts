plugins {
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.serialization") version "1.3.61"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0") // JVM depende
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    create("generate", JavaExec::class) {
        dependsOn("build")
        classpath = sourceSets["test"].runtimeClasspath
        main = "models.simple.SimpleModelKt"
    }

    create("buildDotNet", Exec::class) {
        group = "dotNet"
        workingDir = File(projectDir, "CborTest")
        commandLine = listOf("dotnet", "build", "CborTest.sln")
    }
    create("runDotNet", Exec::class) {
        group = "dotNet"
        workingDir = File(projectDir, "CborTest")
        commandLine = listOf("dotnet", "run", "--project","CborTest/CborTest.csproj")
    }

}
