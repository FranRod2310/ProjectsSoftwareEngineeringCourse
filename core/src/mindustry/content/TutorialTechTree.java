package mindustry.content;

import static mindustry.content.Blocks.*;
import static mindustry.content.TechTree.node;
import static mindustry.content.TechTree.nodeRoot;

public class TutorialTechTree {

    public static void load() {
        Planets.tutorialPlanet.techTree = nodeRoot("tutorial", tutorialCoreShard, () -> {
            node(tutorialConveyor, () -> {
            });
            node(tutorialMechanicalDrill, () -> {
            });
            node(tutorialCopperWall, () -> {
            });
            node(tutorialDuo, () -> {
            });
        });
    }

}
