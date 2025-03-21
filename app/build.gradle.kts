import org.gradle.kotlin.dsl.androidTestUtil

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.apollo)
}

android {
    namespace = "com.example.ramcb"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ramcb"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        testApplicationId = "$applicationId.test"
        testInstrumentationRunner = "io.cucumber.android.runner.CucumberAndroidJUnitRunner"
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
    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    apollo {
        service("rickandmorty") {
            packageName.set("com.example.ramcb.data.repository.graphql")
            introspection {
                endpointUrl.set("https://rickandmortyapi.com/graphql")

                schemaFile.set(file("src/main/graphql/schema.graphqls"))

            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.tooling.preview)

    // extra material icons
    implementation(libs.androidx.material.icons.extended)

    // Material components optimized for TV apps
    implementation(libs.androidx.tv.material)

    // ViewModel in Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Coil
    implementation(libs.coil.compose)

    // JSON parser
    implementation(libs.kotlinx.serialization)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.test.junit4.android)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    ksp(libs.hilt.compiler)

    // GraphQL
    implementation(libs.okhttp)
    implementation(libs.apollo.runtime)

    // Testing
    testImplementation(libs.androidx.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)

    // Instrumentation tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.cucumber)
    androidTestImplementation(libs.cucumber.picocontainer)
    androidTestUtil(libs.test.orchestrator)
}
