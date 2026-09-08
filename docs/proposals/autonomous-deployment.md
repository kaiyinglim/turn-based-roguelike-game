# Autonomous Deployment & Tactical Support System

> Original coursework proposal; proposed mechanics may differ from the final implementation.
The Pitch:
The Autonomous Deployment & Tactical Support System allows contracted workers to deploy autonomous drones, support structures, and tactical devices that continue operating independently after deployment. 
Rather than relying solely on direct player actions, workers can strategically place deployables that influence combat, provide support, create hazards, and modify the environment over time.

The Mechanics:
- Workers can purchase and deploy autonomous units and structures using a common deployment system.
- All deployable entities implement the Deployable abstraction and are activated through DeployAction.
  - Deployable Items (Items in inventory)
  - Deployed Entities (Actors/Ground effects)
- Drones operate independently after deployment using specialised DroneBehaviour implementations.
- Combat Drones automatically search for and attack hostile entities without requiring player input. CombatDroneBehaviour uses distance + status priority system
- Scout Drones identify hostile entities and apply DetectionMarked to improve allied targeting efficiency.
- Repair Drones locate damaged allied units and restore their health.
- Mines can be deployed as environmental traps and remain dormant until activated. Mines activate after the deploying worker leaves the tile
- When triggered, Mines generate surrounding ElectricField hazards and convert the affected location into a DestroyedTile.
- Actors standing on ElectricField terrain receive the EMPShock status effect and damage overtime and may lead to death
- Shield Beacons provide defensive support by granting nearby allies the Shielded status.
- Detection-marked targets are prioritised by autonomous combat systems, enabling coordination between deployed units.
- Deployables continue functioning after deployment, allowing workers to influence the battlefield indirectly.

The Architecture
Abstraction 1: Deployable (Interface) 
Provides a common deployment contract for all deployable structures, traps and autonomous units.

Class	       Status	Role
CombatDroneItem	New	    Deploys a Combat Drone unit
ScoutDroneItem	New	    Deploys a Scout Drone unit
RepairDroneItem	New	    Deploys a Repair Drone unit
Mine	        New	    Deployable trap that creates hazards and EMP effects
Turret	        New	    Deployable defensive structure
ShieldBeacon	New	    Deployable support structure that grants defensive benefits
Drone	        New	    Base autonomous actor class that executes DroneBehaviour each turn

Abstraction 2: DroneBehaviour (Abstract Class)
Provides the common autonomous decision-making framework used by all drone units.

Class	               Status	Role
CombatDroneBehaviour	New	    Offensive behaviour that attacks hostile entities
ScoutDroneBehaviour	    New	    Reconnaissance behaviour that applies DetectionMarked
RepairDroneBehaviour	New	    Support behaviour that repairs allied units

Supporting New Classes (Status & Environment Systems)

Class	       Status	Role
EMPShock	    New	    Prevents affected entities from acting temporarily
DetectionMarked	New	    Marks targets for prioritised targeting
Shielded	    New	    Provides defensive protection
ElectricField	New	    Hazardous terrain that applies EMPShock damage overtime and may lead to death
DestroyedTile	New	    Terrain generated after Mine detonation
DeployAction	New	    Executes deployment of deployable entities 
ElectricAttack	New	    Combat action used by Combat Drone to deal electric damage and apply EMPShock
