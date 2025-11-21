package mindustry.ui;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.MapObjectives.ObjectiveMarker;
import mindustry.game.MapObjectives.ShapeTextMarker;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.input.Binding;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class Marker {


    private BaseDialog dialog;
    private TextField inputField;
    private Table colorTable; // Tabela de cores acessível globalmente

    private boolean initialized = false;

    // Dados temporários
    private float targetX, targetY;
    private Color selectedColor = Pal.accent;

    // Objeto que estamos a editar (se for null, estamos a criar um novo)
    private ShapeTextMarker editingMarker = null;

    // A tua paleta de cores personalizada
    private final Color[] palette = {
            Color.black,
            Color.blue,
            Color.green,
            Color.orange,
            Color.pink,
            Color.yellow,
            Color.white,
            Color.purple,
            Color.red,
            Color.brown,
    };

    public void init() {
        if(initialized) return;

        dialog = new BaseDialog("Marker");

        // --- 1. Campo de Texto ---
        dialog.cont.add("Nota:").left().padBottom(5f).row();

        inputField = new TextField();
        inputField.setMessageText("Escreve aqui...");
        dialog.cont.add(inputField).size(300f, 40f).row();

        // --- 2. Seletor de Cores ---
        dialog.cont.add("Cor:").left().padTop(10f).padBottom(5f).row();

        colorTable = new Table();
        // A tabela de cores será preenchida dinamicamente no openDialog
        dialog.cont.add(colorTable).row();

        initialized = true;
    }

    public void update() {
        // Só corre se estivermos no jogo e sem teclado ativo
        if (!Vars.state.isGame() || Core.scene.hasKeyboard()) return;

        // CASO 1: CRIAR NOVO (Tecla F4 / markerCreate)
        if (Core.input.keyTap(Binding.markerCreate)) {
            if(!initialized) init();

            targetX = Core.input.mouseWorld().x;
            targetY = Core.input.mouseWorld().y;

            openDialog(null); // null = Modo Criação
        }

        // CASO 2: EDITAR EXISTENTE (Clique Esquerdo / select)
        if (Core.input.keyTap(Binding.select)) {
            ShapeTextMarker hovered = findMarkerAtMouse();

            if (hovered != null) {
                if(!initialized) init();
                openDialog(hovered); // Passamos o marcador para editar
            }
        }
    }


    // Configura a janela dependendo se vamos criar ou editar
    private void openDialog(ShapeTextMarker existing) {
        editingMarker = existing;

        // Limpa os botões antigos (Confirmar/Lixo) para recriar
        dialog.buttons.clear();
        dialog.buttons.button("Cancelar", dialog::hide).size(100f, 50f);

        if (existing == null) {
            // --- MODO CRIAR ---
            dialog.title.setText("Novo Marcador");
            inputField.setText("");
            selectedColor = palette[6]; // Começa com Branco (index 6) ou o que preferires

            dialog.buttons.button("Criar", () -> {
                createMarker();
                dialog.hide();
            }).size(100f, 50f).disabled(b -> inputField.getText().isEmpty());

        } else {
            // --- MODO EDITAR ---
            dialog.title.setText("Editar Marcador");
            inputField.setText(existing.text);
            selectedColor = existing.color;

            // Botão de REMOVER (Lixo)
            dialog.buttons.button(Icon.trash, Styles.cleart, () -> {
                removeMarker(existing);
                dialog.hide();
            }).size(50f, 50f).padRight(10f);

            // Botão de SALVAR
            dialog.buttons.button("Salvar", () -> {
                updateExistingMarker();
                dialog.hide();
            }).size(100f, 50f).disabled(b -> inputField.getText().isEmpty());
        }

        // Reconstrói a tabela de cores para selecionar visualmente a cor correta
        rebuildColorTable();

        dialog.show();
        Core.scene.setKeyboardFocus(inputField);
    }

    private void rebuildColorTable() {
        colorTable.clear();
        arc.scene.ui.ButtonGroup<ImageButton> group = new arc.scene.ui.ButtonGroup<>();

        for(Color color : palette){
            ImageButton button = colorTable.button(Tex.whiteui, Styles.clearTogglei, 24f, () -> {
                selectedColor = color;
            }).size(40f).pad(4f).get();

            button.getStyle().imageUpColor = color;
            button.getStyle().imageCheckedColor = color;

            // Se esta for a cor selecionada, marca o botão como ativo visualmente
            if(selectedColor.equals(color)){
                button.setChecked(true);
            }

            group.add(button);
        }
    }

    // Lógica para encontrar um marcador perto do rato
    private ShapeTextMarker findMarkerAtMouse() {
        float worldX = Core.input.mouseWorld().x;
        float worldY = Core.input.mouseWorld().y;
        float range = 20f; // Raio do clique

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

        int id = Mathf.random(10000, 999999);
        Vars.state.markers.add(id, marker);
        Log.info("Marcador criado.");
    }

    private void updateExistingMarker() {
        if (editingMarker != null) {
            editingMarker.text = inputField.getText();
            editingMarker.color = selectedColor;
            // Não mudamos a posição ao editar
        }
    }

    private void removeMarker(ShapeTextMarker marker) {
        // Precisamos encontrar o ID do marcador para removê-lo da lista
        int idToRemove = -1;

        // Itera sobre o mapa interno de IDs do Mindustry
        for(var entry : Vars.state.markers.map.entries()){
            if(entry.value == marker){
                idToRemove = entry.key;
                break;
            }
        }

        if(idToRemove != -1){
            Vars.state.markers.remove(idToRemove);
        }
    }
}