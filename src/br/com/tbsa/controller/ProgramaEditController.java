package br.com.tbsa.controller;

import br.com.tbsa.entity.LinhaSistema;
import br.com.tbsa.entity.Programa;
import br.com.tbsa.service.LinhaSistemaService;
import br.com.tbsa.service.ProgramaService;
import br.com.tbsa.utl.ComboBoxAutoComplete;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

public class ProgramaEditController implements Initializable {

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private Programa programa;
    private ProgramaService programaService;
    private LinhaSistemaService linhaSistemaService;
    private Boolean novoRegistro;

    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXTextField txtNome;
    @FXML
    private JFXComboBox<LinhaSistema> cbLinhaSistema;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        linhaSistemaService = LinhaSistemaService.getInstancia();
        cbLinhaSistema.getItems().addAll(carregarComboLinhaSistemas());
        new ComboBoxAutoComplete<LinhaSistema>(cbLinhaSistema);
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        programaService = ProgramaService.getInstancia();
        try {
            validation();
            programa.setNome(txtNome.getText());
            programa.setLinhaSistema(cbLinhaSistema.getSelectionModel().getSelectedItem());
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Programa", e.getMessage(), NotificationType.ERROR);
        }
    }

    @FXML
    private void handlerCancelar(ActionEvent event) {
        editStage.close();
    }

    public Boolean isbtnConfirmarClicked() {
        return btnConfirmarClicked;
    }

    public Stage getEditStage() {
        return editStage;
    }

    public void setEditStage(Stage editStage) {
        this.editStage = editStage;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
        if (programa.getNome() != null) {
            this.txtNome.setText(programa.getNome());
            JFXComboBox<LinhaSistema> box = null;
            for (int i = 0; i < this.cbLinhaSistema.getItems().size(); i++) {
                if (cbLinhaSistema.getItems().get(i).toString().equalsIgnoreCase(programa.getLinhaSistema().getNome())) {
                    this.cbLinhaSistema.getSelectionModel().select(i);
                    break;
                }
            }
            this.novoRegistro = false;
        } else {
            this.novoRegistro = true;
        }
    }

    private List<LinhaSistema> carregarComboLinhaSistemas() {
        try {
            return linhaSistemaService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Programas", e.getMessage(), NotificationType.ERROR);
            return null;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<Programa> arr = new ArrayList<>();
        arr.addAll(programaService.findAllWithoutClose());
        if (!novoRegistro) {
            arr.removeIf(p -> p.equals(programa));
        }
        if (txtNome.getText().isEmpty()) {
            msg += "Informe um nome.\n";
            control = true;
        }
        if (cbLinhaSistema.getSelectionModel().getSelectedItem() == null) {
            msg += "Escolha uma linha de sistema.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(p -> p.getNome().equalsIgnoreCase(txtNome.getText()))) {
            msg += "Programa já cadastrodo.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }
}
