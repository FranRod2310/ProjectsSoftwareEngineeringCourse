package mindustry.content;

import java.util.LinkedList;
import java.util.List;

import static mindustry.content.Blocks.*;
import static mindustry.content.TechTree.node;
import static mindustry.content.TechTree.nodeRoot;

//US3
public class TutorialTechTree {
private static final List<TechTree.TechNode> nodes = new LinkedList<>();
    public static void load() {
        nodes.add(nodeRoot("tutorial", tutorialCoreShard, () -> {
            nodes.add(node(tutorialConveyor, () -> {
            }));
            nodes.add(node(tutorialMechanicalDrill, () -> {
            }));
            nodes.add(node(tutorialCopperWall, () -> {
            }));
            nodes.add(node(tutorialDuo, () -> {
            }));
        }));
        Planets.tutorialPlanet.techTree = nodes.get(nodes.size() - 1);
    }

    public static List<TechTree.TechNode> getNodes() {
        return nodes;
    }

}
