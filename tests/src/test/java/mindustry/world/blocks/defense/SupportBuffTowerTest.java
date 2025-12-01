package mindustry.world.blocks.defense;

import mindustry.Vars;
import mindustry.ai.BlockIndexer;
import mindustry.core.ContentLoader;
import mindustry.core.GameState;
import mindustry.core.World;
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
        // 1. Initialize Vars.content (Previous fix)
        if (Vars.content == null) {
            Vars.content = new ContentLoader();
        }

        // for block logic that interacts with the map (like applyDamageBoost -> Vars.indexer.eachBlock).
        if (Vars.indexer == null) {
            Vars.indexer = new BlockIndexer();
            Vars.world = new World(); // Required by BlockIndexer or block methods
            Vars.state = new GameState();
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
        assertTrue(resultNoPower, "Must return true when efficiency is 0");

        // Case 2: With power (efficiency = 1)
        build.efficiency = 1f;
        boolean resultHasPower = (boolean) isNotPoweredMethod.invoke(build);
        assertFalse(resultHasPower, "Must return false when efficiency is 1");
    }

    @Test
    @DisplayName("3. Buildup Progress: Should accumulate pulseTimer when powered and stop when unpowered")
    void testBuildupProgress() throws Exception {
        // Accesses the private pulseTimer field via Reflection to monitor progress
        Field pulseTimerField = SupportBuffBuild.class.getDeclaredField("pulseTimer");
        pulseTimerField.setAccessible(true);

        // Test pulse when tower is powered
        build.efficiency = 1f;
        pulseTimerField.set(build, 0.5f); // Set initial pulseTimer state
        float initialPulseTimer = pulseTimerField.getFloat(build);

        // Simulate tile update
        build.updateTile();
        float pulseTimerAfterUpdate = pulseTimerField.getFloat(build);

        // Check if pulseTimer increased
        assertTrue(pulseTimerAfterUpdate > initialPulseTimer, "The accumulation 'pulseTimer' must increase when powered");

        // Test pulse when tower is unpowered
        build.efficiency = 0f;
        pulseTimerField.set(build, 0.6f); // New initial pulseTimer state
        initialPulseTimer = pulseTimerField.getFloat(build);

        // Simulate update
        build.updateTile();
        float pulseTimerAfterNoPower = pulseTimerField.getFloat(build);

        // Check if pulseTimer did not change (because updateTile() returns immediately if not powered)
        assertEquals(initialPulseTimer, pulseTimerAfterNoPower, 0.001f, "The accumulation 'pulseTimer' must not change when unpowered");
    }
    @Test
    @DisplayName("Power Consumption: Values configured correctly")
    void testPowerConsumptionConfig() {
        // Verify if consumption was configured (exact value is in consPower.capacity or usage)
        // Since Mindustry uses a complex consumer system, we verify if it exists
        assertNotNull(tower.consumers, "Consumer list must not be null");
        assertTrue(tower.hasConsumers, "Block must have registered consumers");
    }
}