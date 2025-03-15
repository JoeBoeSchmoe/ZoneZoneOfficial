# 📌 Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]
-  (POST) Create User

## [3.0.0] - 2025-03-15
### Added
- Implemented `GeneralAccountController` with a `GET /accountUsers/listUsers` endpoint.
- Created a response format function to structure API responses properly.
- Added robust error handling and debugging messages for API responses.
- Integrated Basic Authentication for secure access to account-related endpoints.

### Fixed
- Addressed 401 Unauthorized issue by ensuring authentication headers are properly handled.
- Improved logging by replacing `printStackTrace()` with structured logging.

### Notes
- Next step: Expand controller functionality with more account management features.


## [2.0.0] - PostgreSQL Database Setup Complete
### Added
- Successfully installed and configured PostgreSQL on AWS EC2.
- Created a new PostgreSQL database: `zonezonedb`.
- Set up a new PostgreSQL role: `joseph`, with full privileges.
- Verified database existence and successful connection.

### Fixed
- Resolved permission issues preventing PostgreSQL access.
- Ensured `joseph` can log in and manage `zonezonedb`.

## [1.0.6] - 2025-03-14
### Added
- Set up AWS EC2 instance (`t3.small`) for backend development.
- Installed and configured PostgreSQL.
- Installed Java (OpenJDK 17) for Spring Boot.
- Installed Maven for dependency management.
- Ensured PostgreSQL starts on boot and verified functionality.

## [1.0.5] - 2025-03-14
### 🛠 Backend Foundation & Database Setup
- **Initialized Spring Boot project** in `/backend/` with Maven.
- **Configured PostgreSQL database** in `application.properties`.
- **Added Flyway Migration** for database version control.
- **Created `UserAccount` entity** to store user details.
- **Implemented `UserAccountRepository`** for database operations.
- **Established Service Layer** for managing user logic.
- **Set up `UserAccountController`** to handle REST API requests.
- **Resolved Maven compilation issues** due to package structure.
- **Refactored package structure** for cleaner organization:
  - Moved `UserAccount.java` to `entity/` package.
  - Updated `UserAccountRepository` to reference the correct entity path.
- **Verified successful Spring Boot startup** (`mvn spring-boot:run`).



## [1.0.3] - 2025-03-14
### 🔧 Git Repository Structure Updates
- Renamed `main` branch to `primary` to enforce a stable main branch.
- Created `secondary` branch to act as a staging environment for testing before merging to `primary`.
- Deleted the old `main` branch locally and ensured `primary` is properly tracked.
- Configured `secondary` as the base for all future issue branches.
- Verified upstream tracking for both `primary` and `secondary` to ensure correct synchronization with GitHub.


## [1.0.2] - 2025-03-14
### 📝 Documentation Updates
- Added `CHANGELOG.md` to track all project changes.
- Formatted changelog according to [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).
- Ensured **Semantic Versioning** (`MAJOR.MINOR.PATCH`) is followed.


## [1.0.1] - 2025-03-14
### 🎉 Initial Release
- Set up **Git repository**.
- Created `.gitignore` with **industry-standard rules**.
- Organized **project file structure**:
  - `backend/`
  - `game-server/`
  - `frontend/`
  - `assets/`
  - `docs/`
- Added **README.md** and `LICENSE.md`.
- Established professional `.gitignore` formatting.
- Ensured repository is clean and structured for expansion.


## [1.0.0] - 2025-03-14
### 🛠️ Project Initialization
- Created initial GitHub repository.
- Uploaded base project files.
- Defined development workflow.
