buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://jitpack.io"))
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.45")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath ("com.google.gms:google-services:4.3.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://jitpack.io"))
    }
}
