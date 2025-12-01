package testsUS1;

import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.MapMarkers;
import mindustry.game.MapObjectives.ShapeTextMarker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import arc.graphics.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class MarkerTest {

    private MapMarkers markersContainer;

    @BeforeEach
    void setUp() {
        // 1. Initialize Global Vars State
        // MapMarkers requires Vars.state to be accessible in a real game context,
        // but for isolated testing, we initialize the structure directly.
        if (Vars.state == null) {
            Vars.state = new GameState();
        }

        // Initialize the container (Using the parent class logic used by Marker)
        markersContainer = new MapMarkers();

        // Inject this container into Vars.state so static references work if needed
        Vars.state.markers = markersContainer;
    }

    @Test
    @DisplayName("1. Marker Data: Must store correct attributes (Text/Color/Pos)")
    void testMarkerAttributes() {
        // Test UC1/UC2 Data Structure
        ShapeTextMarker marker = new ShapeTextMarker();

        // Simulate Input
        String expectedText = "Attack Here!";
        Vec2 expectedPos = new Vec2(100f, 200f);

        marker.setText(expectedText, false);
        marker.pos.set(expectedPos);

        assertEquals("Attack Here!", marker.text, "Marker text should match input");
        assertEquals(100f, marker.pos.x, "X position should be 100");
        assertEquals(200f, marker.pos.y, "Y position should be 200");
    }

    @Test
    @DisplayName("2. Storage Logic: Add and Retrieve Marker by ID")
    void testAddAndRetrieve() {
        ShapeTextMarker marker = new ShapeTextMarker();
        marker.text = "Base Alpha";
        int uniqueID = 12345;

        // Add to container (Logic used in UC1)
        markersContainer.add(uniqueID, marker);

        // Assertions
        assertTrue(markersContainer.has(uniqueID), "Container must contain the ID after adding");
        assertEquals(marker, markersContainer.get(uniqueID), "Retrieved object must match the added object");
        assertEquals(1, markersContainer.size(), "Size must be 1");
    }

    @Test
    @DisplayName("3. Remove Logic: Must remove marker by Object Reference (New Implementation)")
    void testRemoveByObject() {
        // This tests the specific method added to MapMarkers.java for UC3

        ShapeTextMarker markerA = new ShapeTextMarker();
        markerA.text = "Marker A";
        int idA = 10;

        ShapeTextMarker markerB = new ShapeTextMarker();
        markerB.text = "Marker B";
        int idB = 20;

        // Populate
        markersContainer.add(idA, markerA);
        markersContainer.add(idB, markerB);

        assertEquals(2, markersContainer.size(), "Setup: Must have 2 markers");

        // Action: Remove Marker A using the OBJECT reference, not ID
        // This validates the loop logic implemented in MapMarkers.remove(ObjectiveMarker)
        markersContainer.remove(markerA);

        // Assertions
        assertFalse(markersContainer.has(idA), "Marker A ID should no longer exist");
        assertTrue(markersContainer.has(idB), "Marker B should still exist");
        assertEquals(1, markersContainer.size(), "Size must decrease to 1");
    }
    @Test
    @DisplayName("Color Logic: Must verify marker color assignment")
    void testMarkerColor() {
        ShapeTextMarker marker = new ShapeTextMarker();

        Color expectedColor = Color.orange;

        marker.color = expectedColor;

        assertEquals(expectedColor, marker.color, "The marker should store the assigned color (Orange)");
        assertNotEquals(Color.blue, marker.color, "The marker should not be blue");
    }
}