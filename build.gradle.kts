// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra["kotlin_version"] = "1.3.50"
    extra["ignoreGradle"] = false
    repositories {
        google()
        mavenCentral()
        maven("$rootDir/repo")
    }
    val kotlin_version: String by extra
    val ignoreGradle: Boolean by extra
    val VERSION_NAME: String by extra
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.18.0")
        if (!ignoreGradle) {
            classpath("com.wrbug.kv:kv-gradle:${VERSION_NAME}")
        }
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("$rootDir/repo")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}