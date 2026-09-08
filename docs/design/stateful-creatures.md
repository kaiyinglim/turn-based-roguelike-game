# Stateful Creatures

## Behaviour and transitions 

Group of 5 members: **2** creatures with 4 states each (8 states total).

#### Creature 1: Mannequin — state table

The **Mannequin** is a stateful moon enemy whose mode is chosen each turn by `CreatureState#getNextState` before its ordinary action runs. It keys off **how many workers** are on the eight neighbour tiles and two internal counters: **lonely idle turns** (no adjacent workers while idle) and **no-pickup streak** (collector with no adjacent workers). When the resolved next state differs from the current one, the matching **entry transition effect** runs once, then that state’s `getAction` runs.

| State (`StatefulCreatureStateId`) | Behaviour (ordinary action) | Entry transition effect |
|-----------------------------------|-----------------------------|---------------------------|
| `MANNEQUIN_IDLE` | Do nothing; tracks lonely-idle streak when no adjacent workers | `FreezeNearbyWorkersEffect(2)` |
| `MANNEQUIN_COLLECTOR` | `CollectBehaviour` then `WanderBehaviour`; tracks no-pickup streak | `PullNearbyItemsEffect` |
| `MANNEQUIN_STALKER` | `StalkBehaviour` toward workers | `PullNearbyItemsEffect` |
| `MANNEQUIN_BERSERK` | `AttackBehaviour` vs adjacent workers | `PoisonNearbyWorkersEffect(3)` |

#### Creature 2: Cablebot — state table

The **Cablebot** patrols in **survey** until it either **latches** onto adjacent workers (invisible `CableConnection`s) or **charges** through existing cables when no worker is beside it. In **latch** it attaches or strengthens cables; in **charge** it passively gains **charge** from those cables; at **full charge** with live cables it can enter **overload** to spend charge and disrupt workers. Each turn it ages and prunes broken cables before `StatefulMoonEnemy#playTurn` picks the next behavioural mode, runs any entry effect when the mode changes, then resolves that mode’s ordinary action.

| State (`StatefulCreatureStateId`) | Behaviour (ordinary action) | Entry transition effect |
|-----------------------------------|-----------------------------|---------------------------|
| `CABLEBOT_SURVEY` | Do nothing; decides latch vs charge from adjacency and cables | `PullNearbyItemsEffect` |
| `CABLEBOT_LATCH` | `LatchCableBehaviour` (attach or strengthen cables) | `PoisonNearbyWorkersEffect(5)` |
| `CABLEBOT_CHARGE` | Gains charge from live cables (`DoNothingAction`) | `BurnSurroundingsEffect(1)` |
| `CABLEBOT_OVERLOAD` | `OverloadCableBehaviour` (spend charge, disrupt via cables) | `BurnSurroundingsEffect(2)` |

Transition conditions are **deterministic** - see `CreatureState#getNextState` implementations under `src/main/java/game/states/mannequin/` and `src/main/java/game/states/cablebot/`.

### Effect types (summary)

| Class | Role |
|-------|------|
| `FreezeNearbyWorkersEffect` | Applies `Frozen` to adjacent workers (`WorkerSensing`); workers skip turns via `ContractedWorker#playTurn`. |
| `PullNearbyItemsEffect` | Moves first portable neighbour ground item onto the creature’s tile (`getPickUpAction` probe). |
| `PoisonNearbyWorkersEffect` | Applies `Poison` to adjacent workers in scan order (skips if already poisoned). |
| `BurnSurroundingsEffect` | Places temporary `Fire` on eligible neighbouring ground (intensity scales burst). |

Effects implement `TransitionEffect` and are registered with `StatefulMoonEnemy#addEnteringStateEffect` in `Mannequin` / `Cablebot` constructors; they run in `StatefulMoonEnemy#playTurn` when `nextStateId != activeId`, **before** that turn’s ordinary `getAction`.

### New Behaviours

- **Mannequin:** `CollectBehaviour` (ground pickup) on Collector; `StalkBehaviour` on Stalker.
- **Cablebot:** `LatchCableBehaviour`, `OverloadCableBehaviour`.

