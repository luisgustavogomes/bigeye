package br.com.tbsa.controller;

import br.com.tbsa.entity.So;
import br.com.tbsa.service.SoService;
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

public class SoEditController implements Initializable {

    @FXML
    private JFXTextField txtDescricao;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private So so;
    private SoService soService;
    private Boolean novoRegistro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        soService = SoService.getInstancia();
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        try {
            validation();
            so.setDescricao(txtDescricao.getText());
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("So", e.getMessage(), NotificationType.ERROR);
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

    public So getSo() {
        return so;
    }

    public void setSo(So so) {
        this.so = so;
        if (so.getDescricao() != null) {
            this.txtDescricao.setText(so.getDescricao());
            this.novoRegistro = false;
        } else {
            this.novoRegistro = true;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<So> arr = new ArrayList<>();
        arr.addAll(soService.findAllWithoutClose());
        if (!novoRegistro) {
            arr.removeIf(s -> s.equals(so));
        }
        if (txtDescricao.getText().isEmpty()) {
            msg += "Informe uma descrição";
            control = true;
        }
        if (arr.parallelStream().anyMatch(s -> s.getDescricao().equalsIgnoreCase(txtDescricao.getText()))) {
            msg += "Sistema operacional já cadastrado.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }
}
