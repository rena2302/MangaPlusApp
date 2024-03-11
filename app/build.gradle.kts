plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mangaplusapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mangaplusapp"
        minSdk = 30
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
    buildFeatures{
        viewBinding = true;
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.23") //gif
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2")
    implementation ("androidx.cardview:cardview:1.0.0") // box shadow
    implementation ("io.github.chaosleung:pinview:1.4.4") // input Pin


    implementation ("de.svenkubiak:jBCrypt:0.4.1"); // hash password

    implementation ("com.github.1902shubh:SendMail:1.0.0"); // send email otp

    implementation("com.github.momo-wallet:mobile-sdk:1.0.7")// momo SDK

    implementation("com.squareup.okhttp3:okhttp:4.12.0")// request HTTPs

    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation ("com.google.firebase:firebase-database:20.3.1")

    // Stripe Android SDK
    implementation ("com.stripe:stripe-android:20.39.0")
    implementation ("com.android.volley:volley:1.2.1")

    //===========================================GOOGLE===========================================//
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    //===========================================END GOOGLE=======================================//


}
