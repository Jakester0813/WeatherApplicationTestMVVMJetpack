@file:Suppress("UnstableApiUsage")

import com.android.build.api.variant.BuildConfigField
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

androidComponents {
    onVariants {
        it.buildConfigFields.put(
            "API_KEY", BuildConfigField(
                "String",
                "\"9f4fbb5e6af3a28167fafa1a6ca7f621\"",
                ""
            )
        )
    }
}

android {
    namespace = "com.example.weatherapplicationtest"
    compileSdk = 34

    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    defaultConfig {
        applicationId = "com.example.weatherapplicationtest"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    configurations {
        api.get().exclude(mapOf("group" to "org.jetbrains", "module" to "annotations"))
    }
}

tasks.withType<Test>{
    useJUnitPlatform()
}

kapt {
    correctErrorTypes = true
}

dependencies {

    //Android
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)

    //Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.compiler)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.materail)
    implementation(libs.androidx.compose.materail.iconsExtended)
    implementation(libs.androidx.compose.materail3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.viewbinding)
    implementation(libs.coil.compose)

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.rx2)
    implementation(libs.kotlinx.coroutines.test)

    //Lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.reactivestreams.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Hilt
    implementation(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.androidx.hilt.work)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)

    //Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    //Testing
    testImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent.android)

    //Network
    implementation(libs.retrofit.adapter.rxjava)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp3.logging.interceptor)

    //Misc
    implementation(libs.androidx.work)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.location)
    implementation(libs.timber)

}