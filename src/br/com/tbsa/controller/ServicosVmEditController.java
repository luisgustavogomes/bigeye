package br.com.tbsa.controller;

import br.com.tbsa.entity.Servico;
import br.com.tbsa.entity.ServicosVm;
import br.com.tbsa.service.ServicoService;
import br.com.tbsa.service.ServicosVmService;
import br.com.tbsa.utl.ComboBoxAutoComplete;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import tray.notification.NotificationType;

public class ServicosVmEditController implements Initializable {

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private ServicosVm servicosVm;
    private ServicosVmService servicosVmService;
    private ServicoService servicoService;
    private Boolean novoRegisto;

    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXComboBox<Servico> cbServico;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicoService = ServicoService.getInstancia();
        servicosVmService = ServicosVmService.getInstancia();
        cbServico.getItems().addAll(carregarComboServicos());
        new ComboBoxAutoComplete<Servico>(cbServico);
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        try {
            validation();
            servicosVm.setServico(cbServico.getSelectionModel().getSelectedItem());
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Serviços", e.getMessage(), NotificationType.ERROR);
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

    public ServicosVm getServicosVm() {
        return servicosVm;
    }

    public void setServicosVm(ServicosVm servicosVm) {
        this.servicosVm = servicosVm;
        if (servicosVm.getServico() != null) {
            JFXComboBox<Servico> box = null;
            for (int i = 0; i < cbServico.getItems().size(); i++) {
                if (cbServico.getItems().get(i).toString().equalsIgnoreCase(servicosVm.getServico().getNome())) {
                    this.cbServico.getSelectionModel().select(i);
                    break;
                }
            }
            this.novoRegisto = false;
        } else {
            this.novoRegisto = true;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Servico> carregarComboServicos() {
        try {
            return servicoService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Servicos instalados", e.getMessage(), NotificationType.ERROR);
            return null;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<ServicosVm> arr = new ArrayList<>();
        arr.addAll(servicosVmService.findAllWithoutClose());
        if (!novoRegisto) {
            arr.removeIf(s -> s.equals(servicosVm));
        }
        if (cbServico.getSelectionModel().getSelectedItem() == null) {
            msg = "Escolha um serviço.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(s -> s.getProgramasVm().getPrograma().getNome().equalsIgnoreCase(ProgramasVmController.PROGRAMASVMEDICAO.getPrograma().getNome())
                && s.getServico().getNome().equalsIgnoreCase(cbServico.getSelectionModel().getSelectedItem().getNome()))) {
            msg = "Serviço já incluso.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }

}
