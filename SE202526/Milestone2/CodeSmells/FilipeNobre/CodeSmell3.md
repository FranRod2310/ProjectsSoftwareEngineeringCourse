# Code Smell 3 - Data Clumps

`...\core\src\mindustry\editor\MapView`

## Code Snippet
The `MapView` class defines multiple input handlers (`touchDown`, `touchUp`, `touchDragged`, etc.) that 
repeatedly use the same group of parameters and local variables:
```java
@Override
public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
    if(pointer != 0){
        return false;
    }

    if(!mobile && button != KeyCode.mouseLeft && button != KeyCode.mouseMiddle && button != KeyCode.mouseRight){
        return true;
    }

    if(button == KeyCode.mouseRight){
        lastTool = tool;
        tool = EditorTool.eraser;
    }

    if(button == KeyCode.mouseMiddle){
        lastTool = tool;
        tool = EditorTool.zoom;
    }

    mousex = x;
    mousey = y;

    Point2 p = project(x, y);
    lastx = p.x;
    lasty = p.y;
    startx = p.x;
    starty = p.y;
    tool.touched(p.x, p.y);
    firstTouch.set(p);

    if(tool.edit){
        ui.editor.resetSaved();
    }

    drawing = true;
    return true;
}

@Override
public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
    if(!mobile && button != KeyCode.mouseLeft && button != KeyCode.mouseMiddle && button != KeyCode.mouseRight){
        return;
    }

    drawing = false;

    Point2 p = project(x, y);

    if(tool == EditorTool.line){
        ui.editor.resetSaved();
        tool.touchedLine(startx, starty, p.x, p.y);
    }

    editor.flushOp();

    if((button == KeyCode.mouseMiddle || button == KeyCode.mouseRight) && lastTool != null){
        tool = lastTool;
        lastTool = null;
    }

}

@Override
public void touchDragged(InputEvent event, float x, float y, int pointer){
    mousex = x;
    mousey = y;

    Point2 p = project(x, y);

    if(drawing && tool.draggable && !(p.x == lastx && p.y == lasty)){
        ui.editor.resetSaved();
        Bresenham2.line(lastx, lasty, p.x, p.y, (cx, cy) -> tool.touched(cx, cy));
    }

    if(tool == EditorTool.line && tool.mode == 1){
        if(Math.abs(p.x - firstTouch.x) > Math.abs(p.y - firstTouch.y)){
            lastx = p.x;
            lasty = firstTouch.y;
        }else{
            lastx = firstTouch.x;
            lasty = p.y;
        }
    }else{
        lastx = p.x;
        lasty = p.y;
    }
}
        });
                }
```

### Rationale
The same group of data (`event`, `x`, `y`, `pointer`, `button`, and derived values like `Point2 p`) appears repeatedly across several methods.
This pattern represents a **Data Clump** — a code smell indicating that related data should be encapsulated together rather than passed around separately.

**Problems with this design**:

- **Code repetition**: each method manually handles the same group of variables.
- **Low cohesion and high fragility**: adding a new property (e.g., `deltaX`, `deltaY`, or touch pressure) requires changing all methods.
- **Reduced readability**: repeated parameter lists and local variables obscure the main logic.
- **Lack of abstraction**: there’s no unified concept representing a “user interaction event”.

### Suggested Refactoring
The solution is to encapsulate the input-related data into a single class, for example `InputContext` or `TouchContext`.
This class stores all relevant information for a single interaction, including both raw and derived values.

## New Supporting Class
```java
public class InputContext {
    public final InputEvent event;
    public final float x, y;
    public final int pointer;
    public final KeyCode button;
    public final Point2 projected;

    public InputContext(InputEvent event, float x, float y, int pointer, KeyCode button, Point2 projected){
        this.event = event;
        this.x = x;
        this.y = y;
        this.pointer = pointer;
        this.button = button;
        this.projected = projected;
    }
}
```

## Refactored Usage in MapView
```java
@Override
public boolean touchDown(InputContext ctx, KeyCode button){
    if(pointer != 0){
        return false;
    }

    if(!mobile && button != KeyCode.mouseLeft && button != KeyCode.mouseMiddle && button != KeyCode.mouseRight){
        return true;
    }

    if(button == KeyCode.mouseRight){
        lastTool = tool;
        tool = EditorTool.eraser;
    }

    if(button == KeyCode.mouseMiddle){
        lastTool = tool;
        tool = EditorTool.zoom;
    }

    mousex = ctx.x;
    mousey = ctx.y;

    Point2 p = project(ctx.x, ctx.y);
    lastx = p.x;
    lasty = p.y;
    startx = p.x;
    starty = p.y;
    tool.touched(p.x, p.y);
    firstTouch.set(p);

    if(tool.edit){
        ui.editor.resetSaved();
    }

    drawing = true;
    return true;
}

@Override
public void touchUp(InputContext ctx, KeyCode button){
    if(!mobile && button != KeyCode.mouseLeft && button != KeyCode.mouseMiddle && button != KeyCode.mouseRight){
        return;
    }

    drawing = false;

    Point2 p = project(ctx.x, ctx.y);

    if(tool == EditorTool.line){
        ui.editor.resetSaved();
        tool.touchedLine(startx, starty, p.x, p.y);
    }

    editor.flushOp();

    if((button == KeyCode.mouseMiddle || button == KeyCode.mouseRight) && lastTool != null){
        tool = lastTool;
        lastTool = null;
    }

}

@Override
public void touchDragged(InputContext ctx){
    mousex = ctx.x;
    mousey = ctx.y;

    Point2 p = project(ctx.x, ctx.y);

    if(drawing && tool.draggable && !(p.x == lastx && p.y == lasty)){
        ui.editor.resetSaved();
        Bresenham2.line(lastx, lasty, p.x, p.y, (cx, cy) -> tool.touched(cx, cy));
    }

    if(tool == EditorTool.line && tool.mode == 1){
        if(Math.abs(p.x - firstTouch.x) > Math.abs(p.y - firstTouch.y)){
            lastx = p.x;
            lasty = firstTouch.y;
        }else{
            lastx = firstTouch.x;
            lasty = p.y;
        }
    }else{
        lastx = p.x;
        lasty = p.y;
    }
}
        });
                }
```

## Explanation of the Refactoring
- **Encapsulates all related input data** in a single object (`InputContext`).
- **Removes the repetitive parameter groups** from method signatures.
- **Centralizes input projection logic**, ensuring consistent behavior across all input events.
- **Improves maintainability and extensibility** — new input properties can be added to `InputContext` without changing existing method signatures.

## Benefits
- **Eliminates Data Clumps** — no repeated parameter groups across handlers.
- **Simplifies method signatures**, improving readability.
- **Centralizes input logic** for consistent handling.
- **Reduces change propagation** — only one class changes when input data evolves.
- **Improves clarity and cohesion**, following SRP and DRY principles.