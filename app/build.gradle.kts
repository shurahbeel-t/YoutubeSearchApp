plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.plugins.serialization)
}

android {
    namespace = "com.observer.youtubesearchapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.observer.youtubesearchapp"
        minSdk = 22
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Appends ".debug" to the applicationId for debug builds
            applicationIdSuffix = ".debug"
            // Optional: Appends "-debug" to the version name
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    packaging {
        resources {
            excludes += "META-INF/**.*"
            excludes += "META-INF/*"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // custom
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)

    // for image loading
    implementation(libs.glide)
    implementation(libs.okhttp)

    // google api java client (required? for youtube data api v3's library)
    // implementation(libs.google.api.client) { exclude(group = "org.apache.httpcomponents") }
    // implementation(libs.google.api.client.jackson2) { exclude(group = "org.apache.httpcomponents") }

    // youtube data api v3's google-generated java library
    // implementation(libs.google.api.services.youtube) { exclude(group = "org.apache.httpcomponents") }

    // for communicating with the yt api
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
}