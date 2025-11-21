package mindustry.ui; // Ou o pacote onde estiveres a trabalhar

import arc.Core;
import arc.input.KeyCode;
import arc.scene.ui.TextField;
import arc.util.Log;
import mindustry.ui.dialogs.BaseDialog;

public class Markers {
    private BaseDialog dialog;
    private TextField inputField;

    // executa uma vez so no inicio
    public void init() {
        dialog = new BaseDialog("Novo Marcador");
        dialog.cont.add("Escreve a tua nota abaixo:").row();
        inputField = new TextField();
        inputField.setMessageText("Digita aqui...");
        dialog.cont.add(inputField).size(300f, 50f).row();
        dialog.addCloseButton();
        dialog.hidden(() -> {
            Log.info("Texto digitado: " + inputField.getText());
        });
    }

    // verifica a tecla a cada frame
    public void update() {
        // verifica se a tecla P foi pressionada neste frame e se o jogo não está em uma caixa de texto
        if (Core.input.keyTap(KeyCode.p) && !Core.scene.hasKeyboard()) {
            dialog.show();
        }
    }
}