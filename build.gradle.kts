// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra["kotlin_version"] = "1.3.50"
    extra["ignoreGradle"] = false
    repositories {
        google()
        jcenter()
        maven("$rootDir/repo")
    }
    val kotlin_version: String by extra
    val ignoreGradle: Boolean by extra
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        if (!ignoreGradle) {
            classpath("com.wrbug.kv:kv-gradle:1.0.0")
        }
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("$rootDir/repo")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}