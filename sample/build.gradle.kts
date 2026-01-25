plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.ioffeivan.otp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ioffeivan.otp"
        minSdk = 26
        targetSdk = 36

        val oneTimePasswordVersion = libs.versions.oneTimePassword.get()
        versionCode = generateVersionCode(oneTimePasswordVersion)
        versionName = oneTimePasswordVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        compose = true
    }
}

dependencies {
    implementation(project(":compose"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    debugImplementation(libs.androidx.ui.tooling)
}

enum class ReleaseType(val code: Int) {
    ALPHA(1),
    BETA(2),
    STABLE(5),
    ;

    companion object {
        fun fromSuffix(suffix: String): ReleaseType =
            when (suffix) {
                "" -> STABLE
                "alpha" -> ALPHA
                "beta" -> BETA
                else -> throw IllegalArgumentException("Unknown suffix type: $suffix. Supported: alpha, beta")
            }
    }
}

fun generateVersionCode(version: String): Int {
    val versionRegex = Regex("""^(\d+)\.(\d+)\.(\d+)(?:-([a-z]+)(\d+))?$""")

    val match =
        versionRegex.matchEntire(version)
            ?: throw IllegalArgumentException("Invalid version format: $version. Expected X.Y.Z or X.Y.Z-suffixNN")

    val (majorStr, minorStr, patchStr, suffixStr, iterationStr) = match.destructured

    val major = majorStr.toInt()
    val minor = minorStr.toInt()
    val patch = patchStr.toInt()

    require(minor <= 99) { "Minor version must be <= 99" }
    require(patch <= 99) { "Patch version must be <= 99" }

    val releaseType = ReleaseType.fromSuffix(suffixStr)
    val iteration =
        when (releaseType) {
            ReleaseType.STABLE -> 0
            else -> iterationStr.toInt()
        }

    require(iteration <= 99) { "Iteration must be <= 99" }

    return major * 10_000_000 + minor * 100_000 + patch * 1_000 + releaseType.code * 100 + iteration
}
