# Pīkau

Android app for planning tramping trips. Pīkau is te reo Māori for a backpack, or for carrying something on your back.

I'm building it for my own trips. The interesting part is having Room, navigation, and screen state in one app, with a design in [Figma](https://www.figma.com/design/7UdZ0TfROyJnIYNndzIe0T/Hike-app) and the task list on the [Notion board](https://app.notion.com/p/Hike-App-3ce8d9047aff8087b615f3a62697adc6).

## On screen

Four tabs: Home, Gear, Trips, and Me. Deeper screens slide in, and the bottom bar animates out while you're on one.

- **Gear library.** Search, filter by category, empty state. Categories are seeded on first launch (shelter, sleep, cooking, and so on). New Item does not open a form yet, and the gear detail route is a placeholder.
- **Trips.** Upcoming and past lists, from the trip start date. New trip does not open a form yet, and trip detail is a placeholder.
- **Me.** Greeting, trips completed, and rows for settings, notifications, emergency contact, privacy, open source libraries, and support. Open source libraries is the only row that navigates. The list is generated from the app's runtime dependencies at build time. Distance and average pack weight stay empty until those numbers exist.
- **Home.** Title, tagline, and the next upcoming trip as plain text. Tapping it opens the trip detail placeholder. Search, the trip card, to-do list, and the rest of that screen are still to build.

Loading and full-screen error states are shared, and errors can be retried.

## Already in the database

Room (schema version 1) stores trips, gear, gear categories, trip categories, packing list items, food items, and trip notes. Repositories for packing lists, food, and notes are implemented and covered by tests. None of that is hooked up to a screen yet.

A trip has a name, location, dates, categories, and difficulty. Gear has a name, category, weight in grams, and the trip types it is suggested for.

## Still to build

From the current Figma screens:

- **Home.** Upcoming trip and a to-do list.
- **New trip.** Name, location, dates, categories.
- **Trip detail.** Overview with location, a map, dates, an Open-Meteo forecast, difficulty, trip type, and packing progress. Then packing, food, and notes.
- **New gear item.** Name, category, weight, quantity, and which trip types it is suggested for. Quantity is in the design and not in the schema yet.
- **Gear detail.**
- **Me.** Edit profile, app settings, notification preferences, emergency contact, privacy policy, and contact support. The rows are on screen. The destinations are not.

## Layout

```
app/                 navigation, bottom bar, Hilt application
feature/trips        trips list
feature/gear         gear library
feature/profile      Me, open source libraries
core/model           shared models
core/database        Room
core/data            repositories
core/ui              theme, components, loading and error
core/testing         Main dispatcher rule for ViewModel tests
build-logic          convention plugins
```

```
app
 |
 +-- trips ------+
 +-- gear -------+--> ui
 +-- profile ----+    data --> database --> model
 |                    model
 +-- ui
 +-- data
 +-- model
```

Down the arrows is "depends on". The feature modules do not depend on each other.

## Stack

Kotlin, Jetpack Compose, Material 3, Hilt, Room, Navigation 3, kotlinx.serialization. Shared Android setup lives in the convention plugins. minSdk 26, JDK 17.

## Run and test

Open the project in Android Studio, or:

```
./gradlew :app:assembleDebug
./gradlew test
```

GitHub Actions runs `./gradlew build` and `./gradlew test` on Ubuntu with JDK 17, for pushes and pull requests to `main`.

ViewModel tests use Turbine. Room repositories have their own tests. Compose UI tests cover the button, chip, and tabs.

Roborazzi screenshot tests are set up under `core/ui`, but `./gradlew test` does not compare images. The goldens were recorded on macOS, and Ubuntu renders the edges differently. Record or verify on one machine:

```
./gradlew :core:ui:recordRoborazziDebug
./gradlew :core:ui:verifyRoborazziDebug
```
