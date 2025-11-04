# Design Pattern #2 - Factory Method

## Creator
`core/src/mindustry/type/UnitType`
```java
public class UnitType extends UnlockableContent {

    // Supplier that determines which kind of Unit is created
    public Prov<? extends Unit> constructor = UnitEntity::create;

    public Unit create(Team team){
        Unit unit = constructor.get();
        unit.team = team;
        unit.setType(this);
        if(unit.controller() instanceof CommandAI command && defaultCommand != null){
            command.command = defaultCommand;
        }
        for(var ability : unit.abilities){
            ability.created(unit);
        }
        unit.ammo = ammoCapacity; //fill up on ammo upon creation
        unit.elevation = flying ? 1f : 0;
        unit.heal();
        if(unit instanceof TimedKillc u){
            u.lifetime(lifetime);
        }
        return unit;
    }
```
UnitType defines the factory method create(Team), which standardizes how Unit objects are created.
However, it delegates the exact class of Unit to the constructor supplier, allowing flexible customization by subclasses.

## Concrete Creator A
`core/src/mindustry/type/unit/MissileUnitType`
```java
public class MissileUnitType extends UnitType{

    public MissileUnitType(String name){
        super(name);

        playerControllable = false;
        createWreck = false;
        createScorch = false;
        (...)
        constructor = TimedKillUnit::create;
        (...)
    }
}
```
MissileUnitType redefines the constructor to produce TimedKillUnit objects, making it a Concrete Creator in the Factory Method pattern.

## Concrete Creator B
`core/src/mindustry/type/unit/ErekirUnitType`
```java
public class ErekirUnitType extends UnitType {

    public ErekirUnitType(String name){
        super(name);
        outlineColor = Pal.darkOutline;
        envDisabled = Env.space;
        ammoType = new ItemAmmoType(Items.beryllium);
        researchCostMultiplier = 10f;
    }
}
```
ErekirUnitType doesn’t replace the constructor but customizes the produced unit’s behavior and attributes, representing another Concrete Creator variant.

<img width="2186" height="831" alt="image" src="https://github.com/user-attachments/assets/18b2158c-8647-4a1e-9a9c-af45528f3cdc" />


**Advantages:**
- Encapsulates unit creation logic in one place.
- Allows easy addition of new unit types without modifying core code.



