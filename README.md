# Pīkau

A Kotlin/Jetpack Compose tramping companion app. Current MVP will include trip planning, 
a personal gear library, and meal planning.

> **Pīkau** (te reo Māori) — a backpack, or "to carry on one's back."

## Status

🚧 In active development — MVP in progress.

## Features (MVP)

- **Trips**: create and browse tramping trips
- **Gear Library**: a reusable personal database of gear
- **Packing Lists**: generated per trip from the Gear Library, grouped by category
- **Pre-trip Reminders**: local notification ahead of a trip date

## Future work
- **Meal Library**: a reusable personal database of meals
- **Weather integration**: the updated, predicted weather displayed per trip
- **AllTrails integration**: a reusable personal database of meals
- **Trip to-do list**: a list of tasks per trip with tracked completion 

## Tech stack

| Layer | Choice |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM (ViewModel + StateFlow) |
| DI | Hilt |
| Persistence | Room |
| Background work | WorkManager |
| Navigation | Compose Navigation |
| Testing | JUnit, Turbine, Compose UI Testing |

## Why this project

Built to be genuinely useful for planning my own tramping trips, and to work
through the full Android stack (persistence, background work, and
non-trivial UI state) end to end, rather than following a single tutorial.

## Roadmap

See [project board](#https://app.notion.com/p/Hike-App-3ce8d9047aff8087b615f3a62697adc6?source=copy_link) 
for current task breakdown across design and development.

## Screenshots

_Coming soon._

## License

MIT
