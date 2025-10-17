# OneTimePassword

A library of UI components for one-time password (OTP) input built with Jetpack Compose.

[![Check](https://github.com/ivanioffe/one-time-password/actions/workflows/check.yaml/badge.svg?branch=main)](https://github.com/ivanioffe/one-time-password/actions/workflows/check.yaml)
[![](https://jitpack.io/v/ivanioffe/one-time-password.svg)](https://jitpack.io/#ivanioffe/one-time-password)

## Features

- Flexible input components for OTP codes
- Customizable styles and behavior
- Easy integration with Jetpack Compose projects

## Installation

**Step 1.** Add the JitPack repository to your build file:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

**Step 2.** Add the dependency:

```kotlin
dependencies {
    implementation("com.github.ivanioffe:one-time-password:compose:latest-version")
}
```

## Quick Start

Example usage of the OTP input component:

```kotlin
var otp by remember { mutableStateOf("") }

OtpField(
    otp = otp,
    length = OtpLength(5),
    onOtpChange = { otp = it },
    content = { /* your implementation */ },
)
```

## Requirements

- Kotlin >= 2.0.21
- Jetpack Compose >= 1.9.3

## Contributing

Questions, suggestions, and pull requests are welcome!

## License

MIT License. See LICENSE for details.

## Contact

Author: [Ivan Ioffe](https://github.com/ivanioffe)