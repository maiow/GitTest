// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.1" apply false
    id("com.android.library") version "8.0.1" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.6.0" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
    kotlin("android") version "1.8.21" apply false
    kotlin("plugin.serialization") version "1.8.21" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}


