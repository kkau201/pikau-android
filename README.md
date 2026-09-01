# Pīkau

A Kotlin/Jetpack Compose tramping companion app — trip planning, a personal
gear library, and generated packing lists. Built as a portfolio project while
upskilling toward a senior Android developer role.

> **Pīkau** (te reo Māori) — a backpack, or "to carry on one's back."

## Status

🚧 In active development — MVP in progress.

## Features (MVP)

- **Trips** — create and browse tramping trips
- **Gear Library** — a reusable personal database of gear
- **Packing Lists** — generated per trip from the Gear Library, grouped by category
- **Trip Notes** — free-text notes per trip
- **Pre-trip Reminders** — local notification ahead of a trip date

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
through the full Android stack — persistence, background work, and
non-trivial UI state — end to end, rather than following a single tutorial.

## Roadmap

See [project board](#) for current task breakdown across design and
development.

## Screenshots

_Coming soon._

## License

MIT
