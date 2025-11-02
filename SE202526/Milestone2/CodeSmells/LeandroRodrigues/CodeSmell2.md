# Code Smell 2 - Inappropriate Intimacy

`...\core\src\mindustry\ui\Menus`

## Code Snippet
```java
package mindustry.ui;

import arc.*;
import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

/** Class for handling menus and notifications across the network. Unstable API! */
public class Menus {
    private static final Seq<MenuListener> menuListeners = new Seq<>();
    private static final Seq<TextInputListener> textInputListeners = new Seq<>();

    /** Register a *global* menu listener. If no option is chosen, the option is returned as -1. */
    public static int registerMenu(MenuListener listener) {
        menuListeners.add(listener);
        return menuListeners.size - 1;
    }

    /** Register a *global* text input listener. If no text is provided, the text is returned as null. */
    public static int registerTextInput(TextInputListener listener) {
        textInputListeners.add(listener);
        return textInputListeners.size - 1;
    }

    //do not invoke any of the methods below directly, use Call

    @Remote(variants = Variant.both)
    public static void menu(int menuId, String title, String message, String[][] options) {
        if (title == null) title = "";
        if (message == null) message = "";
        if (options == null) options = new String[0][0];

        ui.showMenu(title, message, options, (option) -> Call.menuChoose(player, menuId, option));
    }

    @Remote(variants = Variant.both)
    public static void followUpMenu(int menuId, String title, String message, String[][] options) {
        if (title == null) title = "";
        if (message == null) message = "";
        if (options == null) options = new String[0][0];

        ui.showFollowUpMenu(menuId, title, message, options, (option) -> Call.menuChoose(player, menuId, option));
    }

    @Remote(variants = Variant.both)
    public static void hideFollowUpMenu(int menuId) {
        ui.hideFollowUpMenu(menuId);
    }

    
    (...)
}
```
### Rationale

The Menus class directly calls methods on UI (showMenu, showFollowUpMenu, showTextInput), creating tight coupling.
This is Inappropriate Intimacy because Menus depends on UI’s internal implementation to display menus, violating encapsulation and making maintenance harder.

### Suggested Refactoring

To fix this, we should introduce an abstraction layer that defines a MenuDisplay interface. 
Menus interacts only with this interface, and UI implements it.

#### Step 1 - Define an Interface

```java
package mindustry.ui;

import arc.func.Intc;

public interface MenuDisplay {
    void showMenu(String title, String message, String[][] options, Intc callback);
    void showFollowUpMenu(int menuId, String title, String message, String[][] options, Intc callback);
    void showTextInput(String title, String message, int length, String def, boolean numeric, boolean allowEmpty, arc.func.Cons<String> confirmed, Runnable closed);
}

```

#### Step 2 - Let UI implement it

```java
public class UI implements MenuDisplay {
    @Override
    public void showMenu(String title, String message, String[][] options, Intc callback){
        // existing implementation
    }

    @Override
    public void showFollowUpMenu(int menuId, String title, String message, String[][] options, Intc callback){
        // existing implementation
    }

    @Override
    public void showTextInput(String title, String message, int length, String def, boolean numeric, boolean allowEmpty, Cons<String> confirmed, Runnable closed){
        // existing implementation
    }
}

```
#### Step 3 - Refactor Menus
```java
public class Menus {
private static MenuDisplay display = Vars.ui; // still global, but decoupled

    public static void menu(int menuId, String title, String message, String[][] options){
        if(title == null) title = "";
        if(message == null) message = "";
        if(options == null) options = new String[0][0];

        display.showMenu(title, message, options, (option) -> Call.menuChoose(player, menuId, option));
    }

    public static void followUpMenu(int menuId, String title, String message, String[][] options){
        display.showFollowUpMenu(menuId, title, message, options, (option) -> Call.menuChoose(player, menuId, option));
    }

    public static void textInput(...){
        display.showTextInput(...);
    }
}
```
This refactoring reduces coupling: Menus no longer knows about UI internals, only the MenuDisplay interface.