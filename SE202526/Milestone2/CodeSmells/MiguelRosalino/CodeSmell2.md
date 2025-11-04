# Smell #2 - Message Chaining
`...\core\src\mindustry\type\Planet`

## Code Snippet

```java
    public void drawSelection(VertexBatch3D batch, Sector sector, Color color, float stroke, float length){
        float arad = (outlineRad + length) * radius;

        for(int i = 0; i < sector.tile.corners.length; i++){
            Corner next = sector.tile.corners[(i + 1) % sector.tile.corners.length];
            Corner curr = sector.tile.corners[i];

            next.v.scl(arad);
            curr.v.scl(arad);
            sector.tile.v.scl(arad);

            Tmp.v31.set(curr.v).sub(sector.tile.v).setLength(curr.v.dst(sector.tile.v) - stroke).add(sector.tile.v);
            Tmp.v32.set(next.v).sub(sector.tile.v).setLength(next.v.dst(sector.tile.v) - stroke).add(sector.tile.v);

            batch.tri(curr.v, next.v, Tmp.v31, color);
            batch.tri(Tmp.v31, next.v, Tmp.v32, color);

            sector.tile.v.scl(1f / arad);
            next.v.scl(1f / arad);
            curr.v.scl(1f /arad);
        }
    }
```

### Rationale

In the `drawSelection(...)` method, we can find multiple cases of the **Message Chaining** code smell.
For example here:
```java
Tmp.v31.set(curr.v).sub(sector.tile.v).setLength(curr.v.dst(sector.tile.v) - stroke).add(sector.tile.v);
```
This chain exposes deep object structures in `Sector`, becoming hard to read and overly-complex.

### Suggested Refactoring
Add method in `Sector` to simplify `drawSelection(...)` logic, getting rid of large foreign object chains in `Planet`.

## Update `Sector`
```java
public class Sector {

(...)

    public void drawSelection(VertexBatch3D batch, Color color, float stroke, float arad){
        for(int i = 0; i < tile.corners.length; i++){
            Corner next = tile.corners[(i + 1) % tile.corners.length];
            Corner curr = tile.corners[i];

            Vec3 currS = Tmp.v31.set(curr.v).scl(arad);
            Vec3 nextS = Tmp.v32.set(next.v).scl(arad);
            Vec3 tileS = Tmp.v33.set(tile.v).scl(arad);

            Vec3 insetCurr = Tmp.v34.set(currS).sub(tileS).setLength(currS.dst(tileS) - stroke).add(tileS);
            Vec3 insetNext = Tmp.v35.set(nextS).sub(tileS).setLength(nextS.dst(tileS) - stroke).add(tileS);

            batch.tri(currS, nextS, insetCurr, color);
            batch.tri(insetCurr, nextS, insetNext, color);
        }
    }
}
```

## Update `Planet`
```java
public void drawSelection(VertexBatch3D batch, Sector sector, Color color, float stroke, float length){
    float arad = (outlineRad + length) * radius;
    sector.drawSelection(batch, color, stroke, arad);
}
```
