package br.com.tbsa.controller;

import br.com.tbsa.entity.Servico;
import br.com.tbsa.service.ServicoService;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

public class ServicoEditController implements Initializable {

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private Servico servico;
    private ServicoService servicoService;
    private Boolean novoRegistro;

    @FXML
    private JFXTextField txtNome;
    @FXML
    private JFXComboBox<String> cbStatus;
    @FXML
    private JFXTextArea txtDescricao;
    @FXML
    private JFXTextField txtExecutavel;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbStatus.getItems().addAll("True", "False");
        cbStatus.getSelectionModel().select(0);
        servicoService = ServicoService.getInstancia();
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        try {
            validation();
            servico.setNome(txtNome.getText());
            servico.setStatus(cbStatus.getSelectionModel().getSelectedItem() == "true" ? true : false);
            servico.setDescricao(txtDescricao.getText());
            servico.setExecutavel(txtExecutavel.getText());
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Serviço", e.getMessage(), NotificationType.ERROR);
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

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        if (servico.getNome() != null) {
            this.txtNome.setText(servico.getNome());
            int numberOfStatus = servico.isStatus() ? 0 : 1;
            this.cbStatus.getSelectionModel().select(numberOfStatus);
            this.txtDescricao.setText(servico.getDescricao());
            this.txtExecutavel.setText(servico.getExecutavel());
            this.novoRegistro = false;
        } else {
            this.novoRegistro = true;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<Servico> arr = new ArrayList<>();
        arr.addAll(servicoService.findAll());
        if (!novoRegistro) {
            arr.removeIf(s -> s.equals(servico));
        }
        if (txtNome.getText().isEmpty()) {
            msg += "Informe um nome.\n";
            control = true;
        }
        if (cbStatus.getSelectionModel().getSelectedItem() == null) {
            msg += "Selecione o status!\n";
            control = true;
        }
        if (txtDescricao.getText().isEmpty()) {
            msg += "Informe uma descrição.\n";
            control = true;
        }
        if (txtExecutavel.getText().isEmpty()) {
            msg += "Informe um caminho do executável.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(s -> s.getNome().equalsIgnoreCase(txtNome.getText()))) {
            msg += "Serviço já cadastrado.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }

}
