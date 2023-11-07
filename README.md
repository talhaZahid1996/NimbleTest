# Nimble Test Project - ReadMe

Welcome to the Nimble Test Project! This repository contains a sample Android application that demonstrates the implementation of various features, including login authentication, OAuth authentication, survey listing, and pull-to-refresh. Below, you'll find instructions on how to run the project and important details regarding its functionality.

## Project Overview

### Implemented Features

1. **Login Authentication Screen**

2. **OAuth Authentication**: OAuth authentication is implemented, including the storage of access tokens for secure user authentication.

3. **Automatic Token Refresh**: The application automatically uses refresh tokens to keep users logged in using the OAuth API.

4. **Home Screen**:
   - Horizontal Scroll Surveys
   - Survey Detail Screen
   - Dynamic Bullets

5. **Authentication Screen Pull-to-Refresh**

### API Configuration

To run the project, add the following lines to your `gradle.properties` file:

```
nimbleKey="ofzl-******************OE"
nimbleSecret="lM***L-m**U-F*************U"
nimbleBaseUrl="https://survey-api.nimblehq.co/api/v1/"
nimbleBiometricKey="abc"
dataStoreKey="abc"
```

Make sure to replace the placeholders with the appropriate API credentials and keys.

### Testing Environment

The project has been built and tested on an Android API 26 emulator. It is recommended to use this environment for running the application.

## How to Run the Project

Follow these steps to run the Android Kotlin Test Project on your local machine:

1. Clone this repository to your local machine:

   ```
   git clone [https://github.com/talhaZahid1996/NimbleTest/](https://github.com/talhaZahid1996/NimbleTest.git)
   ```

2. Open the project in Android Studio or your preferred Android development environment.

3. Add the API configuration details (as mentioned above) to your `gradle.properties` file.

4. Build and run the project on an Android API 26 emulator.

5. Explore the implemented features and functionalities as described above.

## Troubleshooting

If you encounter any issues while setting up or running the project, please feel free to open an issue on this GitHub repository, and we will do our best to assist you.

Enjoy exploring the Android Kotlin Test Project! If you have any questions or feedback, don't hesitate to reach out.
