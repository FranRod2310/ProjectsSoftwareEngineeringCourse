package mindustry.ui;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import mindustry.Vars;
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

    private boolean initialized = false;

    // Dados temporários para criar o marcador
    private float targetX, targetY;
    private Color selectedColor = Pal.accent; // Começa com a cor padrão (Laranja)

    // Lista de cores disponíveis para o jogador escolher
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

        dialog = new BaseDialog("New Marker");

        // --- 1. Campo de Texto ---
        dialog.cont.add("Nota:").left().padBottom(5f).row();

        inputField = new TextField();
        inputField.setMessageText("Escreve aqui..."); // Placeholder
        dialog.cont.add(inputField).size(300f, 40f).row();

        // --- 2. Seletor de Cores ---
        dialog.cont.add("Cor:").left().padTop(10f).padBottom(5f).row();

        Table colorTable = new Table();
        // Cria um grupo de botões para que apenas um fique selecionado de cada vez
        arc.scene.ui.ButtonGroup<ImageButton> group = new arc.scene.ui.ButtonGroup<>();

        for(Color color : palette){
            // Cria um botão quadrado com a imagem branca (para podermos tingir)
            ImageButton button = colorTable.button(Tex.whiteui, Styles.clearTogglei, 24f, () -> {
                selectedColor = color;
            }).size(40f).pad(4f).get();

            // Tinge a imagem do botão com a cor da paleta
            button.getStyle().imageUpColor = color;
            button.getStyle().imageCheckedColor = color; // Mantém a cor quando clicado

            // Adiciona ao grupo
            group.add(button);
        }

        // Adiciona a tabela de cores à janela
        dialog.cont.add(colorTable).row();

        // --- 3. Botões de Ação ---
        dialog.buttons.button("Cancelar", dialog::hide).size(120f, 50f);

        dialog.buttons.button("Confirmar", () -> {
            createMarker();
            dialog.hide();
        }).size(120f, 50f).disabled(b -> inputField.getText().isEmpty()); // Desativa se não tiver texto

        initialized = true;
    }

    public void update() {
        // Verifica F4 (ou a tecla que definiste no Binding)
        if (Vars.state.isGame() && Core.input.keyTap(Binding.markerCreate) && !Core.scene.hasKeyboard()) {
            if(!initialized) init();

            targetX = Core.input.mouseWorld().x;
            targetY = Core.input.mouseWorld().y;

            // Reseta os valores da janela
            inputField.setText("");
            selectedColor = Pal.accent; // Volta ao laranja

            dialog.show();
            Core.scene.setKeyboardFocus(inputField);
        }
    }

    private void createMarker() {
        String texto = inputField.getText();
        if(texto.isEmpty()) return;

        // Cria o marcador usando a classe nativa do jogo
        ShapeTextMarker marker = new ShapeTextMarker();

        marker.text = texto;
        marker.pos.set(targetX, targetY);
        marker.color = selectedColor; // <--- AQUI USAMOS A COR ESCOLHIDA
        marker.radius = 4f;

        // ID aleatório
        int id = Mathf.random(10000, 999999);

        // Adiciona ao jogo
        Vars.state.markers.add(id, marker);

        Log.info("Marcador criado em " + targetX + "," + targetY + " com a cor " + selectedColor.toString());
    }
}