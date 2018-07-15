package br.com.tbsa.controller;

import br.com.tbsa.entity.Programa;
import br.com.tbsa.entity.ProgramasVm;
import br.com.tbsa.entity.Vm;
import br.com.tbsa.service.ProgramaService;
import br.com.tbsa.service.ProgramasVmService;
import br.com.tbsa.service.VmService;
import br.com.tbsa.utl.ComboBoxAutoComplete;
import br.com.tbsa.utl.HelperDate;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import tray.notification.NotificationType;

public class ProgramasVmEditController implements Initializable {

    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private ProgramasVm programasVm;
    private ProgramasVmService programasVmService;
    private ProgramaService programaService;
    private VmService vmService;
    private Boolean novoRegistro;

    @FXML
    private JFXComboBox<Programa> cbPrograma;
    @FXML
    private JFXComboBox<Vm> cbVm;
    @FXML
    private JFXDatePicker dpDatainstalacao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        programaService = ProgramaService.getInstancia();
        cbPrograma.getItems().addAll(carregarComboPrograma());
        new ComboBoxAutoComplete<Programa>(cbPrograma);

        vmService = VmService.getInstancia();
        cbVm.getItems().addAll(carregarComboVm());
        new ComboBoxAutoComplete<Vm>(cbVm);
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        programasVmService = ProgramasVmService.getInstancia();
        try {
            validation();
            programasVm.setPrograma(cbPrograma.getSelectionModel().getSelectedItem());
            programasVm.setVm(cbVm.getSelectionModel().getSelectedItem());
            programasVm.setDataInstalacao(HelperDate.toDate(dpDatainstalacao));
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Programas Instalados", e.getMessage(), NotificationType.ERROR);
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

    public ProgramasVm getProgramasVm() {
        return programasVm;
    }

    public void setProgramasVm(ProgramasVm programasVm) {
        this.programasVm = programasVm;
        if (programasVm.getPrograma() != null) {
            JFXComboBox<Programa> boxPrograma = null;
            for (int i = 0; i < this.cbPrograma.getItems().size(); i++) {
                if (cbPrograma.getItems().get(i).toString().equalsIgnoreCase(programasVm.getPrograma().getNome())) {
                    this.cbPrograma.getSelectionModel().select(i);
                    break;
                }
            }
            for (int i = 0; i < this.cbVm.getItems().size(); i++) {
                if (cbVm.getItems().get(i).toString().equalsIgnoreCase(programasVm.getVm().getNomeVm())) {
                    this.cbVm.getSelectionModel().select(i);
                    break;
                }
            }
            this.dpDatainstalacao.setValue(HelperDate.toLocalDate(programasVm.getDataInstalacao()));
            this.novoRegistro = false;
        } else {
            this.novoRegistro = true;
        }
    }

    private List<Programa> carregarComboPrograma() {
        try {
            return programaService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Programas Instalados", e.getMessage(), NotificationType.ERROR);
            return null;
        }
    }

    private List<Vm> carregarComboVm() {
        try {
            return vmService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Programas Instalados", e.getMessage(), NotificationType.ERROR);
            return null;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<ProgramasVm> arr = new ArrayList<>();
        arr.addAll(programasVmService.findAllWithoutClose());
        if (!novoRegistro) {
            arr.removeIf(p -> p.equals(programasVm));
        }
        if (cbPrograma.getSelectionModel().getSelectedItem() == null) {
            msg += "Escolha um programa.\n";
            control = true;
        }
        if (cbVm.getSelectionModel().getSelectedItem() == null) {
            msg += "Escolha uma VM.\n ";
            control = true;
        }
        if (dpDatainstalacao.getValue() == null) {
            msg += "Escolha uma data.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(p
                -> p.getPrograma().getNome().equalsIgnoreCase(cbPrograma.getSelectionModel().getSelectedItem().getNome())
                && p.getVm().getDescricao().equalsIgnoreCase(cbVm.getSelectionModel().getSelectedItem().getDescricao()))) {
            msg += "Programa já instalado.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }
}
