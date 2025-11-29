package mindustry.world.blocks.defense;

import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.world.blocks.defense.SupportBuffTower;
import mindustry.world.blocks.defense.SupportBuffTower.SupportBuffBuild;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class SupportBuffTowerTest {

    private SupportBuffTower tower;
    private SupportBuffBuild build;

    @BeforeEach
    void setUp() {
        if (Vars.content == null) {
            Vars.content = new ContentLoader();
        }
        // Initialize the new tower for each test
        // We add the System.nanoTime() to ensure a unique name for each test instance
        tower = new SupportBuffTower("support-buff-tower-" + System.nanoTime());

        // Initialize the building
        // It is an inner class so we must initialize it manually
        build = tower.new SupportBuffBuild();
    }

    @Test
    @DisplayName("Must initialize with the correct base attributes")
    void testBaseStats() {
        assertEquals(2.5f, tower.baseDamageMultiplier, "The multiplier must be 2.5");
        assertEquals(60f, tower.buffRange, "The range buff must be 60f");
        assertEquals(120f, tower.buildTime, "The time to build must be 120f or 2 seconds");
        assertTrue(tower.hasPower, "The block must need power");
        assertTrue(tower.solid, "The block must be solid");
        assertTrue(tower.update, "The block must have an update logic");
    }
    @Test
    @DisplayName("Power Logic: Must return true if efficiency is <= 0")
    void testIsNotPoweredLogic() throws Exception {
        // Access private method isNotPowered() via Reflection
        Method isNotPoweredMethod = SupportBuffBuild.class.getDeclaredMethod("isNotPowered");
        isNotPoweredMethod.setAccessible(true);

        // Case 1: No power (efficiency = 0)
        build.efficiency = 0f;
        boolean resultNoPower = (boolean) isNotPoweredMethod.invoke(build);
        System.out.println(resultNoPower);
        assertTrue(resultNoPower, "Must return true when efficiency is 0");

        // Case 2: With power (efficiency = 1)
        build.efficiency = 1f;
        boolean resultHasPower = (boolean) isNotPoweredMethod.invoke(build);
        assertFalse(resultHasPower, "Must return false when efficiency is 1");
    }
}