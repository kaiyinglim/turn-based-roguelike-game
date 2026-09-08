# Moon Weather Crisis System

> Original coursework proposal; proposed mechanics may differ from the final implementation.

The Pitch:

The Moon Weather Crisis System introduces unstable artificial weather caused by broken terraforming systems on the abandoned moons. Weather events such as acid rain, meteor storms, and solar flares affect the map by changing terrain, applying status effects, spreading fire, and interacting with actors and items.
This feature makes the game world feel more dynamic because the environment itself becomes a hazard.
---
The Mechanics:

- A WeatherController manages the active weather event, calling tick() on it each turn and advancing to the next weather when the current one expires.
- Weather events rotate through Acid Rain, Meteor Storm, and Solar Flare, each lasting a fixed number of turns.
- Each weather event scans the map for grounds with a matching WeatherCapability (e.g. ACID_CORRODIBLE, METEOR_TARGET, SOLAR_SOURCE)
- Each event delegates all world changing to a paired WeatherEffect, to maintain a clean separation between targeting and effect logic.

Acid Rain (CorrosiveRainEffect):
- Converts targeted ACID_CORRODIBLE ground tiles into Toxic Waste.
- Each tick, Toxic Waste tiles spread the corrosion to adjacent ACID_CORRODIBLE floors, causing the acid pool to grow outward over time.
- Actors standing on affected tiles accumulate stacking Poison, where the longer they remain, the more damage per tick they receive.

Meteor Storm (MeteorImpactEffect):
- Converts a targeted METEOR_TARGET tile into Fire on impact.
- Applies Burning to all actors within the blast AoE radius and forcibly knocks them to a random adjacent tile.
- Items caught in the AoE are destroyed and their tiles converted into Debris ground, which blocks movement and decays back to Floor after a set number of turns.

Solar Flare (SolarIgnitionEffect):

- Spreads Fire outward from all SOLAR_SOURCE tiles (e.g. existing Fire) to adjacent FLAMMABLE ground each tick.
- Newly created Fire tiles themselves gain SOLAR_SOURCE capability, so next tick they also become spread sources, and causes the fire radius to double with each tick.
- Actors caught in the expanding fire receive Burning.
---
The Architecture:

Abstraction 1: `WeatherEvent` (Abstract Class)

- Manages the weather lifecycle: tracking duration, calling `findTargets(...)` via capability queries, and delegating world mutation to a `WeatherEffect`. 
- Subclasses implement `findTargets(...)` to specify which capability marks their target tiles.

| Class         | Status  | Role                                                |
|---------------|---------|-----------------------------------------------------|
| `AcidRain`    | **New** | Selects `ACID_CORRODIBLE` tiles as targets          |
| `MeteorStorm` | **New** | Selects unoccupied `METEOR_TARGET` tiles as targets |
| `SolarFlare`  | **New** | Selects `SOLAR_SOURCE` tiles (Fire) as targets      |

---

Abstraction 2: `WeatherEffect` (Interface)

Declares the `apply(...)` method. Each implementation mutates the game world.

| Class                 | Status  | Role                                                                                                                 |
|-----------------------|---------|----------------------------------------------------------------------------------------------------------------------|
| `CorrosiveRainEffect` | **New** | Spreads Toxic Waste terrain, applies stacking Poison to actors                                                       |
| `MeteorImpactEffect`  | **New** | AoE Fire + forced actor displacement + Debris terrain                                                                |
| `SolarIgnitionEffect` | **New** | Self-compounding fire spread - new Fire tiles gain `SOLAR_SOURCE`, doubling the radius each tick; actors get Burning |

---

Supporting New Classes 

| Class            | Status  | Role                                                                            |
|------------------|---------|---------------------------------------------------------------------------------|
| `Debris`         | **New** | Blocks movement, decays to `Floor` after N turns                                |
| `StackingPoison` | **New** | Poison status that compounds damage the longer an actor remains on toxic ground |

---

Modified Existing Classes

| Class   | Status          | Modification                                                                                                                                                                    |
|---------|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Floor` | **Retrofitted** | Granted `ACID_CORRODIBLE`, `METEOR_TARGET`, and `FLAMMABLE` capabilities so it participates in all three weather effects without any weather class referencing `Floor` directly |
