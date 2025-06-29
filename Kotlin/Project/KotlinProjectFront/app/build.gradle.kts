plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.frontend"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.frontend"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "mode"
    productFlavors {
        create("server") {
            dimension = "mode"
            applicationIdSuffix = ".server"
            resValue("string", "game", "Games Server")
        }
        create("client") {
            dimension = "mode"
            applicationIdSuffix = ".client"
            resValue("string", "game", "Games Client")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.register("runClient") {
    dependsOn("installClientDebug")
    doLast {
        val adb =
            androidComponents.sdkComponents.adb
                .get()
                .asFile.absolutePath
        exec {
            commandLine(
                adb,
                "shell",
                "am",
                "start",
                "-n",
                "${android.defaultConfig.applicationId}.client.debug/${android.defaultConfig.applicationId}.MainActivity",
            )
        }
    }
}

tasks.register("runServer") {
    dependsOn("installServerDebug")
    doLast {
        val adb =
            androidComponents.sdkComponents.adb
                .get()
                .asFile.absolutePath
        exec {
            commandLine(
                adb,
                "shell",
                "am",
                "start",
                "-n",
                "${android.defaultConfig.applicationId}.server.debug/${android.defaultConfig.applicationId}.MainActivity",
            )
        }
    }
}

dependencies {
    implementation("io.ktor:ktor-client-cio:3.1.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.example:server:1.0")
    implementation("org.example:general:1.0")
    implementation("org.example:client:1.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.junit.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
