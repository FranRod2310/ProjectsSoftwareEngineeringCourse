package mindustry.ui;

import arc.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;

public class Tutorial {

    public enum Stage {
        none,
        welcome,    // UC1 - Entering game tutorial
        building,   // UC2 - Building tutorial
        research,   // UC3 - Research tutorial
        defense,    // UC4 - Defense tutorial
        finished
    }

    public Stage current = Stage.none;


}
