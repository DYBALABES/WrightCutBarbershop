# Wright Cut Barbershop — Mobile Application (OPSC6312 PoE)

![Android CI Build & Automated Unit Tests](https://github.com/DYBALABES/WrightCutBarbershop/actions/workflows/build.yml/badge.svg)


**1. VIDEO DEMONSTRATION**
* Demonstration Video Link (YouTube Unlisted): [https://youtube.com/shorts/znNbYdktn-A?si=cEmYwC5kHXyBh7mT ]
* Demonstration recorded on a physical Android mobile phone with voice-over showcasing client account creation, salted SHA-256 password encryption, offline SQLite database booking, admin live appointment table management (Accept, Reschedule, Decline), dynamic user initials, and real-time runtime language switching into Sesotho.


**2. PROJECT PURPOSE & OVERVIEW**
Wright Cut Barbershop is a native Android mobile application developed strictly using Kotlin and XML Views (Empty Views Activity) with Material 3 components. The app delivers a high-performance digital scheduling, style exploration, and queue-tracking solution for Wright Cut Barbershop located at 932 Monato Street, Ga-Rankuwa View (Zone 15).


**3. CORE INNOVATIVE FEATURES (5 FEATURES TOTAL)**
1. Interactive Appointment Scheduling Flow: Full time-slot booking (08:00 AM – 05:00 PM), interactive calendar date pickers, client detail capture, and persistent local SQLite storage.
2. Style Gallery ("Our Work"): 9-photo real haircut catalog (Mohawk, Classic Taper, Cut & Dye, Beard Sculpt, Bleach & Colour). Tapping any haircut automatically attaches that style reference to the booking screen.
3. Admin Live Appointments Table: Dedicated manager dashboard for reviewing client bookings in real-time with single-tap Accept (green badge), Reschedule (calendar picker), and Decline (red badge) controls.
4. Offline Mode with Two-Way Synchronization: Powered by native Android SQLite (DatabaseHelper), allowing clients to book, reschedule, and cancel appointments completely offline, with automatic synchronization.
5. Live Walk-in Queue Wait Time Estimator: Connects to a RESTful API via Retrofit to calculate dynamic wait times for walk-in clients.


**4. MULTI-LANGUAGE SUPPORT (5 SOUTH AFRICAN LANGUAGES)**
The app provides an in-app runtime language switcher supporting 5 official South African languages:
* English (values/strings.xml)
* Afrikaans (values-af/strings.xml)
* Sesotho (values-st/strings.xml)
* isiZulu (values-zu/strings.xml)
* isiXhosa (values-xh/strings.xml)

Selecting any language updates all headers, cards, quotes, and navigation items live across the entire application.


**5. SECURITY & SESSION CONSIDERATIONS**
* Client-Side Cryptography: Passwords are never stored or transmitted in plaintext. User credentials undergo application-level salting and SHA-256 cryptographic hashing via SecurityUtil.kt.
* Single Sign-On (SSO): Google Single Sign-On flow integrated.
* 2-Hour Auto-Logout: SessionManager.kt monitors user inactivity. Sessions exceeding 2 hours are automatically invalidated for security, prompting the user to re-authenticate.


**6. DEVOPS, AUTOMATED TESTING & GITHUB ACTIONS**
* Automated Unit Testing: BarberUnitTests.kt executes comprehensive test suites covering both positive (valid input) and negative (error/malformed input) cases for email validation, password length, and hash consistency.
* GitHub Actions CI: Every push and pull request triggers .github/workflows/build.yml in a clean Ubuntu runner (JDK 17), running unit tests and compiling the signed debug APK artifact.


**7. RELEASE NOTES (PROTOTYPE VS. FINAL POE)**
* What's New in this Final Release:
  * Added the full Admin Appointments Table with status badges (Pending, Accepted, Declined) and 3-dot action popups.
  * Added 5 South African language runtime switcher with immediate activity recreation.
  * Added Offline SQLite caching layer for users and bookings with DatabaseHelper.
  * Added dynamic account initials on the avatar badge (KM, MA, etc.).
  * Added real-image social media footer icons (gram, fb, tiktok, twitter, app).
  * Verified signed APK generation with 25-year RSA release keystore (wrightcut_key.jks).



**8. AI-USE DISCLOSURE STATEMENT**

During the conceptualization, development, and testing of the Wright Cut Barbershop mobile application for the OPSC6312 Portfolio of Evidence, generative artificial intelligence tools (specifically Claude and ChatGPT) were utilized responsibly as developer assistants to optimize technical rigor and verify compliance with IIE curriculum specifications.

Artificial intelligence tools were utilized in the following capacities:
1. Architecture Migration & Refactoring: Translating UI mockups into strict native Kotlin Android Views (Empty Views Activity) and clean XML layouts using Material 3 components, ensuring complete elimination of disallowed declarative frameworks (Jetpack Compose).
2. Security & Cryptographic Hashing: Formulating the salted SHA-256 cryptographic hashing logic inside SecurityUtil.kt to ensure user credentials undergo client-side encryption prior to local database persistence or remote transmission.
3. Localization Support: Translating technical barbershop terminology across all five supported South African languages (English, Afrikaans, Sesotho, isiZulu, and isiXhosa) to guarantee cultural accuracy in the respective strings.xml resource trees.
4. Offline Persistence & Queue Management: Designing the SQLite persistence schema (DatabaseHelper.kt) to ensure transactional reliability when offline bookings are stored and retrieved locally on physical mobile devices.
5. DevOps & Continuous Integration: Generating and debugging the automated GitHub Actions YAML workflow script (.github/workflows/build.yml) to ensure unit test execution and artifact compilation within remote Ubuntu runner environments.

All AI-suggested code snippets, architectural layouts, and configurations were manually inspected, customized, integrated, and validated inside Android Studio. Every module underwent hands-on functional testing on physical Android hardware and local JUnit runners to ensure authentic student authorship and adherence to the assessment brief.
