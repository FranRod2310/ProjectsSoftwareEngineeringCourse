# Design Pattern #3 - Template

## Abstract Class
`core/src/mindustry/io/SaveFileReader`

```java
public abstract class SaveFileReader {

    public abstract void read(DataInputStream stream, CounterInputStream counter, WorldContext context)
        throws IOException;

    public abstract void write(DataOutputStream stream)
        throws IOException;
}

```
The class defines reusable algorithms for reading/writing structured binary data (“chunks” or “regions”) in a consistent way.

`reading`
```java
    public void readRegion(String name, DataInput stream, CounterInputStream counter, IORunner<DataInput> cons) throws IOException{
        counter.resetCount();
        int length;
        try{
            length = readChunk(stream, (chunkStream, len) -> cons.accept(chunkStream));
        }catch(Throwable e){
            throw new IOException("Error reading region \"" + name + "\".", e);
        }

        if(length != counter.count - 4){
            throw new IOException("Error reading region \"" + name + "\": read length mismatch. Expected: " + length + "; Actual: " + (counter.count - 4));
        }
    }

    public int readChunk(DataInput input, IORunnerLength<DataInput> runner) throws IOException{
        int length = input.readInt();
        runner.accept(input, length);
        return length;
    }

```
`writing`
```java
    public void writeRegion(String name, DataOutput stream, IORunner<DataOutput> cons) throws IOException{
        try{
            writeChunk(stream, writes -> cons.accept(writes.output));
        }catch(Throwable e){
            throw new IOException("Error writing region \"" + name + "\".", e);
        }
    }

    public void writeChunk(DataOutput output, IORunner<Writes> runner) throws IOException{
        boolean wasNested = chunkNested;
        chunkNested = true;
        ReusableByteOutStream dout = wasNested ? byteOutput2 : byteOutput;

        try{
            dout.reset();
            runner.accept(wasNested ? writes2 : writes1);
            int length = dout.size();
            output.writeInt(length);
            output.write(dout.getBytes(), 0, length);
        }finally{
            chunkNested = wasNested;
        }
    }
```
This **abstract** class defines the fixed algorithm for reading and writing save data.
Provides **template methods** like `readRegion()` / `writeRegion()`.

## Concrete implementation
`core/src/mindustry/io/SaveVersion`

`SaveVersion` provides a **concrete implementation** of the abstract `read()` and `write()` methods defined in abstract `SaveFileReader`.

`reading - concrete implementation`
```java
    public void read(DataInputStream stream, CounterInputStream counter, WorldContext context) throws IOException{
        readRegion("meta", stream, counter, in -> readMeta(in, context));
        readRegion("content", stream, counter, this::readContentHeader);

        try{
            readRegion("map", stream, counter, in -> readMap(in, context));
            readRegion("entities", stream, counter, this::readEntities);
            if(version >= 8) readRegion("markers", stream, counter, this::readMarkers);
            readRegion("custom", stream, counter, this::readCustomChunks);
        }finally{
            content.setTemporaryMapper(null);
        }
    }
```
`writing - concrete implementation`
```java
    public void write(DataOutputStream stream, StringMap extraTags) throws IOException{
        writeRegion("meta", stream, out -> writeMeta(out, extraTags));
        writeRegion("content", stream, this::writeContentHeader);
        writeRegion("map", stream, this::writeMap);
        writeRegion("entities", stream, this::writeEntities);
        writeRegion("markers", stream, this::writeMarkers);
        writeRegion("custom", stream, s -> writeCustomChunks(s, false));
    }
```

## Rationale

Defines the skeleton of an algorithm in a base class and allows subclasses to redefine certain steps without changing the algorithm’s overall structure.

<img width="669" height="921" alt="image" src="https://github.com/user-attachments/assets/730dfcf2-f89e-40a4-9ff7-5237e502fccd" />


**Advantages:**
- Enforces a uniform structure for all save files.
- Allows subclasses to customize specific steps.

