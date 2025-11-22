package mindustry.ui;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.MapMarkers;
import mindustry.game.MapObjectives.ObjectiveMarker;
import mindustry.game.MapObjectives.ShapeTextMarker;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.input.Binding;
import mindustry.ui.dialogs.BaseDialog;

public class Marker extends MapMarkers {
    private BaseDialog dialog;
    private TextField inputField;
    private Table colorTable;
    private boolean initialized = false;
    private float targetX, targetY;
    private Color selectedColor = Pal.accent;
    private ShapeTextMarker editingMarker = null;

    private final Color[] palette = {
            Color.black, Color.blue, Color.green, Color.orange, Color.pink,
            Color.yellow, Color.white, Color.purple, Color.red, Color.brown,
    };

    public void init() {
        if(initialized) return;
        dialog = new BaseDialog("Marker");
        dialog.cont.add("Nota:").left().padBottom(5f).row();
        inputField = new TextField();
        inputField.setMessageText("Escreve aqui...");
        dialog.cont.add(inputField).size(300f, 40f).row();

        dialog.cont.add("Cor:").left().padTop(10f).padBottom(5f).row();
        colorTable = new Table();
        dialog.cont.add(colorTable).row();

        initialized = true;
    }

    public void update() {
        if (!Vars.state.isGame() || Core.scene.hasKeyboard()) return;
        // CRIAR COM F3
        if (Core.input.keyTap(Binding.markerCreate)) {
            if(!initialized) init();
            targetX = Core.input.mouseWorld().x;
            targetY = Core.input.mouseWorld().y;
            openDialog(null);
        }
        // EDITAR COM F4
        if (Core.input.keyTap(Binding.markerEdit)) {
            ShapeTextMarker hovered = findMarkerAtMouse();
            if (hovered != null) {
                if(!initialized) init();
                openDialog(hovered);
            }
        }
    }
    private void openDialog(ShapeTextMarker existing) {
        editingMarker = existing;
        dialog.buttons.clear();
        dialog.buttons.button("Cancelar", dialog::hide).size(100f, 50f);

        if (existing == null) {
            // CREATE
            dialog.title.setText("Novo Marcador");
            inputField.setText("");
            selectedColor = palette[6]; // Default (ex: Branco)

            dialog.buttons.button("Criar", () -> {
                createMarker();
                dialog.hide();
            }).size(100f, 50f).disabled(b -> inputField.getText().isEmpty());

        } else {
            // EDIT
            dialog.title.setText("Editar Marcador");
            inputField.setText(existing.text);
            selectedColor = existing.color;

            dialog.buttons.button(Icon.trash, Styles.clearNonei, () -> {
                removeMarker(existing);
                dialog.hide();
            }).size(50f).padRight(10f);

            dialog.buttons.button("Salvar", () -> {
                updateExistingMarker();
                dialog.hide();
            }).size(100f, 50f).disabled(b -> inputField.getText().isEmpty());
        }
        rebuildColorTable();
        dialog.show();
        Core.scene.setKeyboardFocus(inputField);
    }

    // Reconstrói os botões de cor para mostrar qual está selecionado
    private void rebuildColorTable() {
        colorTable.clear();
        arc.scene.ui.ButtonGroup<ImageButton> group = new arc.scene.ui.ButtonGroup<>();

        for(Color color : palette){
            ImageButton button = colorTable.button(Tex.whiteui, Styles.clearTogglei, 24f, () -> {
                selectedColor = color;
            }).size(40f).pad(4f).get();

            button.getStyle().imageUpColor = color;
            button.getStyle().imageCheckedColor = color;

            // Marca o botão como "checkado" se for a cor atual
            if(selectedColor.equals(color)){
                button.setChecked(true);
            }
            group.add(button);
        }
    }
    // Procura marcador perto do rato
    private ShapeTextMarker findMarkerAtMouse() {
        float worldX = Core.input.mouseWorld().x;
        float worldY = Core.input.mouseWorld().y;
        float range = 24f;

        for (ObjectiveMarker m : Vars.state.markers) {
            if (m instanceof ShapeTextMarker) {
                ShapeTextMarker sm = (ShapeTextMarker) m;
                if (Mathf.dst(worldX, worldY, sm.pos.x, sm.pos.y) < range) {
                    return sm;
                }
            }
        }
        return null;
    }
    private void createMarker() {
        String texto = inputField.getText();
        if(texto.isEmpty()) return;

        ShapeTextMarker marker = new ShapeTextMarker();
        marker.text = texto;
        marker.pos.set(targetX, targetY);
        marker.color = selectedColor;
        marker.radius = 4f;

        // gerar id aleatorio mas verficar para caso existir gerar outro
        int id;
        do {
            id = Mathf.random(10000, 999999);
        } while (Vars.state.markers.has(id));
        Vars.state.markers.add(id, marker);
    }

    private void updateExistingMarker() {
        if (editingMarker != null) {
            editingMarker.setText(inputField.getText(),true);
            editingMarker.color = selectedColor;
        }
    }

    private void removeMarker(ShapeTextMarker marker) {
        Vars.state.markers.remove(marker);
    }
}