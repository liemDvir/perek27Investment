plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.perek27"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.perek27"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    packagingOptions {
        resources {
            excludes += "META-INF/DEPENDENCIES"
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.common)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation("com.jjoe64:graphview:4.2.2")
    implementation("com.google.guava:guava:32.1.2-jre")
    api("org.apache.juneau:juneau-marshall:8.2.0")

    //implementation("com.android.support:appcompat-v7:27.1.1")
    //implementation("com.android.support:support-compat:27.1.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    //implementation("com.google.android.gms:play-services-base:18.4.0")

    implementation("com.jjoe64:graphview:4.2.2") {
        exclude(group = "com.android.support")
    }
}