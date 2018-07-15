package br.com.tbsa.controller;

import br.com.tbsa.entity.LinhaSistema;
import br.com.tbsa.service.LinhaSistemaService;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import tray.notification.NotificationType;

public class LinhaSistemaEditController implements Initializable {

    @FXML
    private JFXTextField txtNome;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private LinhaSistema linhaSistema;
    private LinhaSistemaService linhaSistemaService;
    private Boolean novoRegisto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        linhaSistemaService = LinhaSistemaService.getInstancia();
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        try {
            validation();
            linhaSistema.setNome(txtNome.getText());
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Linha Sistema", e.getMessage(), NotificationType.ERROR);
        }
    }

    @FXML
    private void handlerCancelar(ActionEvent event) {
        editStage.close();
    }

    public Stage getEditStage() {
        return editStage;
    }

    public void setEditStage(Stage editStage) {
        this.editStage = editStage;
    }

    public Boolean isbtnConfirmarClicked() {
        return btnConfirmarClicked;
    }

    public LinhaSistema getLinhaSistema() {
        return linhaSistema;
    }

    public void setLinhaSistema(LinhaSistema linhaSistema) {
        this.linhaSistema = linhaSistema;
        if (linhaSistema.getNome() != null) {
            this.txtNome.setText(linhaSistema.getNome());
            this.novoRegisto = false;
        } else {
            this.novoRegisto = true;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<LinhaSistema> arr = new ArrayList<>();
        arr.addAll(linhaSistemaService.findAllWithoutClose());
        if (!novoRegisto) {
            arr.removeIf(l -> l.equals(linhaSistema));
        }
        if (txtNome.getText().isEmpty()) {
            msg += "Informe um nome.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(l -> l.getNome().equalsIgnoreCase(txtNome.getText()))) {
            msg += "linha de sistema já cadastrada.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }

}
