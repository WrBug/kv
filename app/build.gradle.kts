plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}
val kotlin_version: String by project.rootProject.extra
val ignoreGradle: Boolean by project.rootProject.extra

if (!ignoreGradle) {
    apply(plugin = "com.wrbug.kv.gradle")
}


android {
    compileSdkVersion(31)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId("com.wrbug.kv.sample")
        minSdkVersion(16)
        targetSdkVersion(31)
        versionCode(1)
        versionName("1.0")

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
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

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("androidx.appcompat:appcompat:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.tencent:mmkv:1.2.11")
    implementation(project(":kv-annotation"))
    implementation(project(":kv"))
    implementation(project(":test_module"))
    kapt(project(":kv-compile"))
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0")
}