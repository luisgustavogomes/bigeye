package br.com.tbsa.controller;

import br.com.tbsa.entity.Ambiente;
import br.com.tbsa.service.AmbienteService;
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

public class AmbienteEditController implements Initializable {

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private Ambiente ambiente;
    private AmbienteService ambienteService;
    private Boolean novoRegistro;

    @FXML
    private JFXTextField txtDescricao;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ambienteService = AmbienteService.getInstancia();
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        try {
            validation();
            ambiente.setDescricao(txtDescricao.getText());
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Ambiente", e.getMessage(), NotificationType.ERROR);
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

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
        if (ambiente.getDescricao() != null) {
            this.txtDescricao.setText(ambiente.getDescricao());
            this.novoRegistro = false;
        } else {
            this.novoRegistro = true;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<Ambiente> arr = new ArrayList<>();
        arr.addAll(ambienteService.findAllWithoutClose());
        if (!novoRegistro) {
            arr.removeIf(a -> a.equals(ambiente));
        }
        if (txtDescricao.getText().isEmpty()) {
            msg += "Informe uma descrição.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(a -> a.getDescricao().equalsIgnoreCase(txtDescricao.getText()))) {
            msg += "Ambiente já cadastrado.\n";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }

}
