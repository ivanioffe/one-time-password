# OneTimePassword

A library of UI components for one-time password (OTP) input built with Jetpack Compose.

[![Check](https://github.com/ivanioffe/one-time-password/actions/workflows/check.yaml/badge.svg)](https://github.com/ivanioffe/one-time-password/actions/workflows/check.yaml)
[![](https://jitpack.io/v/ivanioffe/one-time-password.svg)](https://jitpack.io/#ivanioffe/one-time-password)

## Features

- Highly customizable OTP input component for Jetpack Compose.
- Stateless design for straightforward integration and efficient state management.
- Slot-based API for defining unique UI elements for each OTP cell based on value, position, or focus.
- Support for various OTP code lengths with flexible cell spacing and arrangement.
- Custom input filtering and validation to ensure secure and valid entries.
- Easy control over keyboard options and action handling for improved user experience.
- Flexible visual transformations, including masking, temporary revealing, or fully custom display logic.
- Advanced focus management with customizable cursor effects like blinking or fading animations.

## Sample project

It's worth exploring the sample app, which includes an interactive playground screen for the OtpField component. You can tweak settings like OTP length, visual transformations, cursor effects, and layout options in real-time to see how they affect the UI and behavior.

Download the Android sample app [here](https://github.com/ivanioffe/one-time-password/releases/latest/download/sample.apk)

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
    implementation("com.github.ivanioffe.one-time-password:compose:latest-version")
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
    content = { /* your cell UI implementation */ },
)
```

## Requirements

- Kotlin >= 2.2.21
- Jetpack Compose >= 1.10.1

## Documentation

Detailed API documentation is available here:
**[View Full Documentation](https://ivanioffe.github.io/one-time-password/)**

## Contributing

Questions, suggestions, and pull requests are welcome!

## License

MIT License. See [LICENSE](https://github.com/ivanioffe/one-time-password/blob/main/LICENCE) for details.

## Contact

Author: [Ivan Ioffe](https://github.com/ivanioffe)
