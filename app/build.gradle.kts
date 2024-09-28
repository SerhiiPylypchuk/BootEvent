plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.spylypchuk.testbootevent"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.spylypchuk.testbootevent"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.workManager)
    implementation(libs.workManager.ktx)

    implementation(libs.room)
    implementation(libs.room.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    ksp(libs.room.compiler)

    implementation(project.dependencies.platform((libs.koin.bom)))
    implementation(libs.koin)
    implementation(libs.koin.workmanager)

    implementation(libs.joda)
}