package br.com.tbsa.controller;

import br.com.tbsa.entity.Cluster;
import br.com.tbsa.entity.So;
import br.com.tbsa.service.ClusterService;
import br.com.tbsa.service.SoService;
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

public class ClusterEditController implements Initializable {

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private Cluster cluster;
    private ClusterService clusterService;
    private SoService soService;
    private Boolean novoRegistro;

    @FXML
    private JFXTextField txtDescricao;
    @FXML
    private JFXTextField txtIpi;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXComboBox<So> cbSo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clusterService = ClusterService.getInstancia();
        soService = SoService.getInstancia();
        cbSo.getItems().addAll(carregarComboSo());
        new ComboBoxAutoComplete<So>(cbSo);
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        try {
            validation();
            cluster.setDescricao(txtDescricao.getText());
            cluster.setIpCluster(txtIpi.getText());
            cluster.setSo(cbSo.getSelectionModel().getSelectedItem());
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

    public Cluster getCluster() {
        return cluster;
    }

    @SuppressWarnings("unchecked")
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
        if (cluster.getDescricao() != null) {
            this.txtDescricao.setText(cluster.getDescricao());
            this.txtIpi.setText(cluster.getIpCluster());
            JFXComboBox<So> cb = null;
            for (int i = 0; i < this.cbSo.getItems().size(); i++) {
                if (cbSo.getItems().get(i).toString().equalsIgnoreCase(cluster.getSo().getDescricao())) {
                    this.cbSo.getSelectionModel().select(i);
                    break;
                }
            }
            this.novoRegistro = false;
        } else {
            this.novoRegistro = true;
        }
    }

    private List<So> carregarComboSo() {
        try {
            return soService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Cluster", e.getMessage(), NotificationType.ERROR);
            return null;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<Cluster> arr = new ArrayList<>();
        arr.addAll(clusterService.findAllWithoutClose());
        if (!novoRegistro) {
            arr.removeIf(c -> c.equals(cluster));
        }
        if (txtDescricao.getText().isEmpty()) {
            msg += "Informe uma descrição.\n";
            control = true;
        }
        if (txtIpi.getText().isEmpty()) {
            msg += "Informe o Ip.\n ";
            control = true;
        }
        if (cbSo.getSelectionModel().getSelectedItem() == null) {
            msg += "Escolha um Sistema Operacional.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(c
                -> c.getDescricao().equalsIgnoreCase(txtDescricao.getText())
                && c.getIpCluster().equalsIgnoreCase(txtIpi.getText()))) {
            msg += "Cluster já cadastrado.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(c -> c.getDescricao().equalsIgnoreCase(txtDescricao.getText()))) {
            msg += "Descrição já cadastrado.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }

}
