plugins {
    id("java-library")
    id("kotlin")
}
apply(from = "$rootDir/maven.gradle")
val kotlin_version: String by project.rootProject.extra

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
}