plugins {
    id("java-library")
    id("kotlin")
}
apply(from = "$rootDir/maven.gradle")

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}
val kotlin_version: String by project.rootProject.extra


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("com.squareup:javapoet:1.11.1")
    implementation(project(":kv-annotation"))
    implementation(project(":kv-runtime"))
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation(files("${System.getProperty("java.home")}/../lib/tools.jar"))
}