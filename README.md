# MangaPlusApp

A comprehensive Android manga reading application built with Java and following the MVP (Model-View-Presenter) architecture pattern. The app provides both user and admin functionalities for managing and reading manga content.

## 🏗️ Architecture

The application follows the **MVP (Model-View-Presenter)** architectural pattern for clean separation of concerns and maintainable code structure.

## ✨ Features

### User Features
- **Authentication System**
  - OTP verification for Sign in/Sign up
  - OTP verification for forgot password
  - Social login integration (Google, Zalo)
  - Biometric authentication support

- **Content Discovery**
  - Search functionality 
  - Hot/trending manga section
  - Category browsing
  - Favorites management
  - Region/country selection

- **Reading Experience**
  - Chapter viewing and navigation
  - Manga detail pages
  - Library management
  - Reading progress tracking

- **Payment & Premium**
  - Multiple payment methods (MoMo, Stripe)
  - Premium content access
  - Purchase history

- **Additional Features**
  - Banner advertisements
  - Music player integration
  - Profile management
  - Permission handling
  - Support center

### Admin Features
- **Content Management (CRUD)**
  - Add/Edit/Delete manga
  - Chapter management
  - Category management
  - Content editor with rich text support

- **Dashboard**
  - Admin dashboard interface
  - Payment control and monitoring
  - User management

## 🛠️ Technical Stack

### Frontend
- **Language**: Java
- **UI Framework**: Android Native
- **Architecture**: MVP Pattern
- **View Binding**: Enabled for type-safe view references

### Backend & Database
- **Database**: Firebase (NoSQL)
- **Authentication**: Firebase Auth
- **Storage**: Firebase Storage

### APIs & Integrations
- **Payment**: MoMo API, Stripe API
- **Social Login**: Zalo SDK, Google API
- **Analytics**: Google Services

### Development Tools
- **Build System**: Gradle with Kotlin DSL
- **Min SDK**: 30
- **Target SDK**: 34
- **Permissions**: Internet, Media access, External storage, Biometric

## 📱 App Structure

```
app/
├── Activity/
│   ├── Admin/          # Admin panel activities
│   ├── Base/           # Base activity classes
│   └── User/           # User-facing activities
├── Adapter/            # RecyclerView adapters
├── Database/           # Database helper classes
├── Fragment/           # UI fragments
├── Helper/             # Utility helper classes
├── ModelAndPresenter/  # MVP pattern implementation
├── object/             # Data models
└── util/              # Utility classes
```

## 🔧 Setup Instructions

### Prerequisites
- Android Studio
- JDK 8 or higher
- Firebase project setup

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Add your `google-services.json` file to the `app/` directory
4. Configure your signing key:
   ```bash
   keytool -list -v -keystore "path/to/key.jks" -alias key0 -storepass your_password -keypass your_password
   ```
5. Add the SHA-1 fingerprint to your Firebase project
6. Build and run the application

### Build Configuration
- **Release APK**: Located in `app/release/app-release.apk`
- **Keystore**: `key.jks` (password protected)
- **Signing Config**: Configured for release builds

## 🔐 Security Features
- Biometric authentication
- Secure payment processing
- ProGuard obfuscation for release builds
- Encrypted user data storage

## 📄 License
This project is developed by Android Mate team.

## 🤝 Contributing
Please follow the established MVP architecture pattern when contributing to this project.

## 📞 Support
For support and queries, please contact through the in-app support center.

---

**Note**: This application requires proper Firebase configuration and API keys for full functionality.
