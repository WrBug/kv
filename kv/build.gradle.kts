plugins {
    id("com.android.library")
    kotlin("android")
}
apply(from = "$rootDir/maven.gradle")

android {
    compileSdkVersion(31)
    buildToolsVersion("30.0.3")

    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(31)
        versionCode(1)
        versionName("1.0")

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
val kotlin_version: String by project.rootProject.extra

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    api("com.google.code.gson:gson:2.8.9")
    api(project(":kv-runtime"))
    api(project(":kv-annotation"))
}