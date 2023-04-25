plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.mivanovskaya.gittest"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.mivanovskaya.gittest"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        //testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
    }
}

dependencies {

    //implementation 'androidx.room:room-runtime:2.5.1'
    //annotationProcessor 'androidx.room:room-compiler:2.5.1'

    // To use Kotlin annotation processing tool (kapt)
    //kapt 'androidx.room:room-compiler:2.5.1'

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    implementation("androidx.fragment:fragment-ktx:1.5.7")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.45")
   // implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:2.45")

    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    //implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    //implementation 'androidx.paging:paging-runtime:3.1.1'

    //testImplementation 'junit:junit:4.13.2'
    //androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    //androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
}