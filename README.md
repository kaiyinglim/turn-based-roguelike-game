# Garbage Collection Inc

A console-based, turn-based roguelike game built in Java. Guide contracted workers across hazardous moon terrain, collect and deposit salvage, purchase equipment, and survive hostile creatures and changing weather.

Developed as group coursework in object-oriented design and implementation. This repository presents the final version, built on earlier iterations.

## Features

- **Salvage and progression:** collect items, trade at a supercomputer, deposit salvage toward company quotas, and use tools such as a plasma cutter.
- **Stateful enemies:** Mannequin and Cablebot switch between four behavioural states, with effects triggered by state transitions.
- **Autonomous support:** deploy combat, scout, and repair drones, alongside mines, turrets, and shield beacons.
- **Environmental hazards:** acid rain, meteor storms, and solar flares alter terrain and apply status effects.
- **Evolving terrain:** growing trees and enemy spawners change the threats workers encounter.
- **Weather integration:** a Java HTTP client retrieves precipitation data from Open-Meteo; Gson parses the response to influence in-game rain.

## Technology

Java 17 · Maven · JUnit Jupiter · Mockito · Gson · Supplied teaching engine

## Run locally

Install **JDK 17 or later** and **Maven**, then open a terminal in the repository root.

```sh
mvn compile dependency:copy-dependencies -DincludeScope=runtime
```

Run on macOS or Linux:

```sh
java -cp "target/classes:target/dependency/*" game.Application
```

Run on Windows:

```powershell
java -cp "target/classes;target/dependency/*" game.Application
```

Alternatively, open `pom.xml` as a Maven project in IntelliJ IDEA, select JDK 17+, load the dependencies, and run `game.Application`.

The game uses a text map and turn-by-turn action menus. Enter the displayed key for an available action.

The live precipitation feature makes synchronous network requests during gameplay. An internet connection is needed for successful weather retrieval, and slow responses may delay turns. The implementation reports request failures in the console.

## Tests

The repository includes JUnit Jupiter tests using Mockito, covering areas such as salvage actions, item interactions, enemy behaviour, tree growth, status effects, and weather effects.

To explicitly select a JUnit 5-compatible test runner:

```sh
mvn org.apache.maven.plugins:maven-surefire-plugin:3.5.3:test
```

## Design

The implementation extends the supplied engine through focused game packages:

| Area | Responsibility |
| --- | --- |
| `actions/` | Player and creature actions |
| `behaviours/` | Reusable actor decision-making |
| `states/` | Creature states and transitions |
| `interfaces/` | Contracts for capabilities such as deployment and trading |
| `deployables/` | Drones, traps, and support structures |
| `weather/` | Weather events, effects, and external data |
| `systems/` | Company quota logic |

Creature states separate mode-specific behaviour from the enemy classes. Weather events select targets and delegate changes to effect implementations. Interfaces such as `Deployable`, `Purchasable`, and `Sellable` provide shared contracts for different game entities.

## Repository structure

```text
src/main/java/game/                 Game implementation
src/main/java/edu/                 Supplied engine and demonstrations
src/test/java/game/                 Unit tests
docs/design/final/                 Final iteration diagrams and rationale
docs/design/earlier-iteration/     Earlier design documentation
docs/proposals/                     Original feature proposals
pom.xml                            Maven configuration
```

## Documentation

- [Final design rationale](docs/design/final/design-rationale.pdf)
- [Quota and salvage UML](docs/design/final/quota-and-salvage-uml.pdf)
- [Scrap snatcher and tree growth UML](docs/design/final/scrap-snatcher-and-tree-growth-uml.pdf)
- [Autonomous deployment UML](docs/design/final/autonomous-deployment-uml.pdf)
- [Moon weather crisis UML](docs/design/final/moon-weather-crisis-uml.pdf)
- [Stateful creature behaviour](docs/design/stateful-creatures.md)
- [Autonomous deployment proposal](docs/proposals/autonomous-deployment.md)
- [Moon weather crisis proposal](docs/proposals/moon-weather-crisis.md)
- [Real-world weather proposal](docs/proposals/real-world-weather.md)

The proposals and PDF reports preserve the original coursework discussions, including requirement labels and designs that may differ from the final code.

## Credits and project context

This is collaborative coursework, built on a supplied teaching engine. Game features represent the team's combined work; this repository does not claim sole authorship. Existing source-level author credits and the AI-assistance disclosure in `WeatherData` are retained.
