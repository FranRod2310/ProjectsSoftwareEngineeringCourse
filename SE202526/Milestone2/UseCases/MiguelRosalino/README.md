# Use Case Diagram - Modding System

**System**: Mindustry Modding System

This diagram represent the modding experience in Mindustry, envolving creating mods and playing them.

**Actors**:
- Mod Developer (creates, edits and test mods)
- Player (installs and plays mods)

## Modding System Use Case Diagram
<img width="2046" height="1057" alt="image" src="https://github.com/user-attachments/assets/117a2270-4ea7-4801-a04d-301a06509efc" />


## UC1 - Create new mod
**Name**: Create new mod

**Brief description**: Mod Dev creates a new mod for Mindustry by defining its metadata (name, version, author)

`<<includes>> UC2 (Define mod custom content)` - Dev can define custom content at creation.

**Main actor**: Mod Developer

**Secondary actors**: none

## UC2 - Define mod custom content
**Name**: Define mod custom content

**Brief description**: Mod Dev defines new content they want to edit/add as a mod. Defines blocks, items and/or units in JSON files.

**Main actor**: Mod Developer

**Secondary actors**: none

## UC3 - Test mod ingame
**Name**: Test mod ingame

**Brief description**: Mod Dev loads the game to check if mod runs correctly. Check for bugs.

`<<includes>> U6 (Enable mod)` - Mod must be enabled ingame for testing.

**Main actor**: Mod Developer

**Secondary actors**: none

## UC4 - Package mod
**Name**: Package mod

**Brief description**: Mod Dev packages the mod into a `.zip` file. Uploads it to a repository.

**Main actor**: Mod Developer

**Secondary actors**: none

## UC5 - Import mod
**Name**: Import mod

**Brief description**: Player imports mod from git or `.zip` into the game.

**Main actor**: Player

**Secondary actors**: none

## UC6 - Enable mod
**Name**: Enable mod

**Brief description**: Player enables a mod ingame from the Mods menu.

**Main actor**: Player

**Secondary actors**: none

