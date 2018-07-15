package br.com.tbsa.controller;

import br.com.tbsa.entity.ProgramasVmHst;
import br.com.tbsa.service.ProgramasVmHstService;
import br.com.tbsa.service.ProgramasVmService;
import br.com.tbsa.utl.HelperDate;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
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

public class ProgramasVmHstEditController implements Initializable {

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private ProgramasVmHst programasVmHst;
    private ProgramasVmHstService programasVmHtsService;
    private Boolean novoRegistro;

    @FXML
    private JFXTextField txtVersao;
    @FXML
    private JFXDatePicker dpDataAtualizacao;
    @FXML
    private JFXTextArea txtObservacao;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        programasVmHtsService = ProgramasVmHstService.getInstancia();
        try {
            validation();
            programasVmHst.setVersao(txtVersao.getText());
            programasVmHst.setDtatualizacao(HelperDate.toDate(dpDataAtualizacao));
            programasVmHst.setObservacao(txtObservacao.getText());
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Histórico", e.getMessage(), NotificationType.ERROR);
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

    public ProgramasVmHst getProgramasVmHst() {
        return programasVmHst;
    }

    public void setProgramasVmHst(ProgramasVmHst programasVmHst) {
        this.programasVmHst = programasVmHst;
        if (programasVmHst.getVersao() != null) {
            this.txtVersao.setText(programasVmHst.getVersao());
            this.dpDataAtualizacao.setValue(HelperDate.toLocalDate(programasVmHst.getDtatualizacao()));
            this.txtObservacao.setText(programasVmHst.getObservacao());
            this.novoRegistro = false;
        } else {
            this.novoRegistro = true;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<ProgramasVmHst> arr = new ArrayList<>();
        arr.addAll(programasVmHtsService.findAllWithoutClose());
        if (!novoRegistro) {
            arr.removeIf(p -> p.equals(programasVmHst));
        }
        if (txtVersao.getText().isEmpty()) {
            msg += "Informe uma versão\n";
            control = true;
        }
        if (dpDataAtualizacao.getValue() == null) {
            msg += "Informe a data de atualização\n";
            control = true;
        }
        if (txtObservacao.getText().length() > 4000) {
            msg += "Capacidade de 4000 caracteres.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(p -> p.getVersao().equalsIgnoreCase(txtVersao.getText()))) {
            msg += "Histórico já cadastrado";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }
}
