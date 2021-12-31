plugins {
    id("groovy")
    id("kotlin")
}
apply(from = "$rootDir/maven.gradle")

val kotlin_version: String by project.rootProject.extra


java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

val gradleVersion = "3.1.4"

dependencies {
    implementation(localGroovy())
    implementation(gradleApi())
    implementation("com.android.tools.build:gradle:$gradleVersion")
    implementation("com.android.tools.build:builder:$gradleVersion")
    implementation("org.javassist:javassist:3.25.0-GA")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
}
