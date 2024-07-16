
buildscript {
    val agp_version by extra("8.2.2")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application") version "8.2.2" apply false
    id ("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.kapt") version "1.8.10" apply false
    id("androidx.navigation.safeargs") version "2.7.7" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}