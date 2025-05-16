plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.android.coingecko.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.android.coingecko.android"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Shared Multiplatform Module
    implementation(projects.shared)

    // Jetpack Compose Core & UI
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)

    // Material Design 2 (for Icons.Filled.Sync and other icons)
    implementation(libs.compose.material)
    implementation(libs.material.icons.extended)

    debugImplementation(libs.compose.ui.tooling)

    // Activity & Compose Runtime
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.runtime.android)

    // Lifecycle & ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Networking (Ktor)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Image Loading
    implementation(libs.coil.compose)

    // Room (Optional)
    implementation(libs.androidx.room.external.antlr)
}
