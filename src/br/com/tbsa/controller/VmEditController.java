package br.com.tbsa.controller;

import br.com.tbsa.entity.Ambiente;
import br.com.tbsa.entity.Cluster;
import br.com.tbsa.entity.So;
import br.com.tbsa.entity.Vm;
import br.com.tbsa.service.AmbienteService;
import br.com.tbsa.service.ClusterService;
import br.com.tbsa.service.SoService;
import br.com.tbsa.service.VmService;
import br.com.tbsa.utl.ComboBoxAutoComplete;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import tray.notification.NotificationType;

public class VmEditController implements Initializable {

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private Vm vm;
    private VmService vmService;
    private ClusterService clusterService;
    private SoService soService;
    private AmbienteService ambienteService;
    private Boolean novoRegistro;

    @FXML
    private JFXTextField txtNome;
    @FXML
    private JFXTextField txtIpi;
    @FXML
    private JFXTextField txtDescricao;
    @FXML
    private JFXComboBox<So> cbSo;
    @FXML
    private JFXComboBox<Ambiente> cbAmbiente;
    @FXML
    private JFXComboBox<Cluster> cbCluster;
    @FXML
    private JFXToggleButton tbServidorBD;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vmService = VmService.getInstancia();

        soService = SoService.getInstancia();
        cbSo.getItems().addAll(carregarComboSo());
        new ComboBoxAutoComplete<So>(cbSo);

        ambienteService = AmbienteService.getInstancia();
        cbAmbiente.getItems().addAll(carregarComboAmbiente());
        new ComboBoxAutoComplete<Ambiente>(cbAmbiente);

        clusterService = ClusterService.getInstancia();
        cbCluster.getItems().addAll(carregarComboCluster());
        new ComboBoxAutoComplete<Cluster>(cbCluster);
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        try {
            validation();
            vm.setNomeVm(txtNome.getText());
            vm.setIpVm(txtIpi.getText());
            vm.setDescricao(txtDescricao.getText());
            vm.setSo(cbSo.getSelectionModel().getSelectedItem());
            vm.setAmbiente(cbAmbiente.getSelectionModel().getSelectedItem());
            vm.setCluster(cbCluster.getSelectionModel().getSelectedItem());
            vm.setServidorBd(tbServidorBD.isSelected());
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Vm", e.getMessage(), NotificationType.ERROR);
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

    public Vm getVm() {
        return vm;
    }

    public void setVm(Vm vm) {
        this.vm = vm;
        if (vm.getDescricao() != null) {
            this.txtNome.setText(vm.getNomeVm());
            this.txtIpi.setText(vm.getIpVm());
            this.txtDescricao.setText(vm.getDescricao());
            JFXComboBox<So> boxSo = null;
            for (int i = 0; i < cbSo.getItems().size(); i++) {
                if (cbSo.getItems().get(i).getDescricao()
                        .equalsIgnoreCase(vm.getSo().getDescricao())) {
                    this.cbSo.getSelectionModel().select(i);
                    break;
                }
            }
            JFXComboBox<Ambiente> boxAmbiente = null;
            for (int i = 0; i < cbAmbiente.getItems().size(); i++) {
                if (cbAmbiente.getItems().get(i).getDescricao()
                        .equalsIgnoreCase(vm.getAmbiente().getDescricao())) {
                    this.cbAmbiente.getSelectionModel().select(i);
                    break;
                }
            }
            JFXComboBox<Cluster> boxCluster = null;
            for (int i = 0; i < cbCluster.getItems().size(); i++) {
                if (cbCluster.getItems().get(i).getDescricao()
                        .equalsIgnoreCase(vm.getCluster().getDescricao())) {
                    this.cbCluster.getSelectionModel().select(i);
                    break;
                }
            }
            this.tbServidorBD.setSelected(vm.isServidorBd());
            this.novoRegistro = false;
        } else {
            this.novoRegistro = true;
        }
    }

    private List<So> carregarComboSo() {
        try {
            return soService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Vm", e.getMessage(), NotificationType.ERROR);
            return null;
        }
    }

    private List<Ambiente> carregarComboAmbiente() {
        try {
            return ambienteService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Vm", e.getMessage(), NotificationType.ERROR);
            return null;
        }
    }

    private List<Cluster> carregarComboCluster() {
        try {
            return clusterService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Vm", e.getMessage(), NotificationType.ERROR);
            return null;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<Vm> arr = new ArrayList<>();
        arr.addAll(vmService.findAllWithoutClose());
        if (!novoRegistro) {
            arr.removeIf(v -> v.equals(vm));
        }
        if (txtNome.getText().isEmpty()) {
            msg += "Informe um nome.\n";
            control = true;
        }
        if (txtIpi.getText().isEmpty()) {
            msg += "Informe o Ip.\n";
            control = true;
        }
        if (txtDescricao.getText().isEmpty()) {
            msg += "Informe uma descrição.\n";
            control = true;
        }
        if (cbSo.getSelectionModel().getSelectedItem() == null) {
            msg += "Escolha um Sistema Operacional.\n";
            control = true;
        }
        if (cbAmbiente.getSelectionModel().getSelectedItem() == null) {
            msg += "Escolha um Ambiente.\n";
            control = true;
        }
        if (cbCluster.getSelectionModel().getSelectedItem() == null) {
            msg += "Escolha o cluster.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(v -> v.getNomeVm().equalsIgnoreCase(txtDescricao.getText()))) {
            msg += "Vm já cadastrada!.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }
}
