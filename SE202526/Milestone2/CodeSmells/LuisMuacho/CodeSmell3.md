# Code Smell 3 - Feature Envy
`...\mindustry\ai\BaseRegistry`
## Code Snippet
Code Snippet with little references where exists envy. 
```java
package mindustry.ai;

import (...);

public class BaseRegistry{
    (...)

    public void load(){
        (...)
        
        for(String name : names){
            try{
                Schematic schem = Schematics.read(Core.files.internal("baseparts/" + name));

                BasePart part = new BasePart(schem);//a 'part' object is created
                Tmp.v1.setZero();
                int drills = 0;

                for(Stile tile : schem.tiles){
                    //keep track of core type
                    if(tile.block instanceof CoreBlock){
                        part.core = tile.block;//Envy: setting 'part' data
                    }

                    //save the required resource based on item source - multiple sources are not allowed
                    if(tile.block instanceof ItemSource){
                        Item config = (Item)tile.config;
                        if(config != null) part.required = config; //Envy: Setting 'part' data
                    }

                    (...)

                    //calculate averages
                    if(tile.block instanceof Drill || tile.block instanceof Pump){
                        Tmp.v1.add(tile.x*tilesize + tile.block.offset, tile.y*tilesize + tile.block.offset);
                        drills ++;
                    }
                }
                (...)
                //Envy: calculating 'part' data externally
                part.tier = schem.tiles.sumf(s -> Mathf.pow(s.block.buildTime / s.block.buildCostMultiplier, 1.4f));

                if(part.core != null){
                    cores.add(part);
                }else if(part.required == null){
                    parts.add(part);
                }

                if(drills > 0){
                    Tmp.v1.scl(1f / drills).scl(1f / tilesize);
                    //Envy: setting part data
                    part.centerX = (int)Tmp.v1.x;
                    part.centerY = (int)Tmp.v1.y;
                }else{
                    part.centerX = part.schematic.width/2;
                    part.centerY = part.schematic.height/2;
                }

                if(part.required != null && part.core == null){
                    reqParts.get(part.required, Seq::new).add(part);
                }

            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }

        (...)
    }
    (...)
}
```
### Rationale
The Feature Envy smell is identified in the load() method, which exhibits an obsessive interest in the internal data of the BasePart class. After creating a new BasePart object, the load() method proceeds to manually populate all of its fields (part.core, part.required, part.tier, part.centerX, part.centerY) by analyzing the schematic itself. This logic clearly belongs inside BasePart. BasePart should be responsible for initializing itself from a Schematic, but instead, BaseRegistry has "stolen" this behavior. This high behavioral coupling is the direct cause of BasePart being reduced to a passive Data Class, as all its potential behavior now lives externally in BaseRegistry.
### Suggested Refactoring
The refactoring to fix this is Move Method (or more accurately, moving the logic into the constructor). The initialization logic must be moved from load() into the BasePart constructor, giving the class responsibility for itself and enforcing encapsulation.

**Steps**:
- **1. Make BasePart fields private.**: This is the first step to proper encapsulation.
- **2. Move all analysis logic** (the for loop over schem.tiles, the tier calculation, the centerX/centerY logic) from the load() method into the BasePart(Schematic schematic) constructor.
- **3.** The BasePart constructor will now be responsible for analyzing its own schematic and setting its own private fields. 
- **4. Add public getters to BasePart** (eg. getCorte(), getRequired()) so the load() method can ask for this information instead of setting it.
- **5.** The load() method becomes dramatically simpler, responsible only for orchestrating the loading, not doing the analysis.
```java
// Inside BaseRegistry.java
public void load(){
    (...) // Unchanged setup logic
    String[] names = Core.files.internal("basepartnames").readString().split("\n");

    for(String name : names){
        try{
            Schematic schem = Schematics.read(Core.files.internal("baseparts/" + name));

            // STEP 5: 'load()' is simplified. It just creates the 'part'.
            // All the envious logic that was here has been moved.
            BasePart part = new BasePart(schem);

            // STEP 5: Logic now uses getters, asking 'part' for its state.
            if(part.getCore() != null){
                cores.add(part);
            }else if(part.getRequired() == null){
                parts.add(part);
            }

            // STEP 5: Logic now uses getters.
            if(part.getRequired() != null && part.getCore() == null){
                reqParts.get(part.getRequired(), Seq::new).add(part);
            }

        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    (...) // Unchanged sorting logic
}

// Inside BasePart.java
public static class BasePart implements Comparable<BasePart>{
    public final Schematic schematic;

    // STEP 1: Fields are now private to enforce encapsulation.
    private int centerX, centerY;
    private @Nullable Content required;
    private @Nullable Block core;
    private float tier;

    // STEP 2 & 3: All logic is moved from 'load()' into the constructor.
    public BasePart(Schematic schematic){
        this.schematic = schematic;

        // This is the logic that was moved from BaseRegistry.load()
        Tmp.v1.setZero();
        int drills = 0;

        for(Stile tile : schematic.tiles){ // 'schematic' instead of 'schem'
            if(tile.block instanceof CoreBlock){
                this.core = tile.block;
            }
            if(tile.block instanceof ItemSource){
                Item config = (Item)tile.config;
                if(config != null) this.required = config;
            }
            //same for liquids - this is not used yet
            if(tile.block instanceof LiquidSource){
                Liquid config = (Liquid)tile.config;
                if(config != null) this.required = config;
            }
            if(tile.block instanceof Drill || tile.block instanceof Pump){
                Tmp.v1.add(tile.x*tilesize + tile.block.offset, tile.y*tilesize + tile.block.offset);
                drills ++;
            }
        }

        schematic.tiles.removeAll(s -> s.block.buildVisibility == BuildVisibility.sandboxOnly);

        this.tier = schematic.tiles.sumf(s -> Mathf.pow(s.block.buildTime / s.block.buildCostMultiplier, 1.4f));

        if(drills > 0){
            Tmp.v1.scl(1f / drills).scl(1f / tilesize);
            this.centerX = (int)Tmp.v1.x;
            this.centerY = (int)Tmp.v1.y;
        }else{
            this.centerX = this.schematic.width/2;
            this.centerY = this.schematic.height/2;
        }
    }

    // STEP 4: Public getters are added to allow read-only access.
    public @Nullable Content getRequired(){ return required; }
    public @Nullable Block getCore(){ return core; }
    public float getTier(){ return tier; }
    public int getCenterX(){ return centerX; }
    public int getCenterY(){ return centerY; }

    @Override
    public int compareTo(BasePart other){
        // Logic unchanged, but now compares the private 'tier' field
        return Float.compare(tier, other.tier);
    }
}
```