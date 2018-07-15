package br.com.tbsa.controller;

import br.com.tbsa.entity.Perfil;
import br.com.tbsa.service.PerfilService;
import br.com.tbsa.utl.HelperNotification;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tray.notification.NotificationType;

public class PerfilEditController implements Initializable {

    @FXML
    private JFXTextField txtDescricao;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private Pane pane;

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private Perfil perfil;
    private PerfilService perfilService;
    private Boolean novoRegistro;
    @FXML
    private JFXCheckBox criarCluster;
    @FXML
    private JFXCheckBox criarVm;
    @FXML
    private JFXCheckBox criarSo;
    @FXML
    private JFXCheckBox criarAmbiente;
    @FXML
    private JFXCheckBox criarProgramasvm;
    @FXML
    private JFXCheckBox criarProgramas;
    @FXML
    private JFXCheckBox criarServicos;
    @FXML
    private JFXCheckBox criarUsuarios;
    @FXML
    private JFXCheckBox criarPerfil;
    @FXML
    private JFXCheckBox criarDashbord;
    @FXML
    private JFXCheckBox editarCluster;
    @FXML
    private JFXCheckBox editarVm;
    @FXML
    private JFXCheckBox editarSo;
    @FXML
    private JFXCheckBox editarAmbiente;
    @FXML
    private JFXCheckBox editarProgramasvm;
    @FXML
    private JFXCheckBox editarProgramas;
    @FXML
    private JFXCheckBox editarServicos;
    @FXML
    private JFXCheckBox editarUsuarios;
    @FXML
    private JFXCheckBox editarPerfil;
    @FXML
    private JFXCheckBox editarDashbord;
    @FXML
    private JFXCheckBox excluirCluster;
    @FXML
    private JFXCheckBox excluirVm;
    @FXML
    private JFXCheckBox excluirSo;
    @FXML
    private JFXCheckBox excluirAmbiente;
    @FXML
    private JFXCheckBox excluirProgramasvm;
    @FXML
    private JFXCheckBox excluirProgramas;
    @FXML
    private JFXCheckBox excluirServicos;
    @FXML
    private JFXCheckBox excluirUsuarios;
    @FXML
    private JFXCheckBox excluirPerfil;
    @FXML
    private JFXCheckBox excluirDashbord;
    @FXML
    private JFXCheckBox visualizarCluster;
    @FXML
    private JFXCheckBox visualizarVm;
    @FXML
    private JFXCheckBox visualizarSo;
    @FXML
    private JFXCheckBox visualizarAmbiente;
    @FXML
    private JFXCheckBox visualizarProgramasvm;
    @FXML
    private JFXCheckBox visualizarProgramas;
    @FXML
    private JFXCheckBox visualizarServicos;
    @FXML
    private JFXCheckBox visualizarUsuarios;
    @FXML
    private JFXCheckBox visualizarPerfil;
    @FXML
    private JFXCheckBox visualizarDashbord;
    @FXML
    private JFXCheckBox criarLinhaSistema;
    @FXML
    private JFXCheckBox editarLinhaSistema;
    @FXML
    private JFXCheckBox excluirLinhaSistema;
    @FXML
    private JFXCheckBox visualizarLinhaSistema;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        perfilService = PerfilService.getInstancia();
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) throws Exception {
        try {
            validation();
            perfil.setCodperfil(txtDescricao.getText());
            perfil.setTextoMenu(perfilService.getTextoMenu(this.getListaPerfil()));
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Perfil", e.getMessage(), NotificationType.ERROR);
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

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
        if (perfil.getCodperfil() != null) {
            this.txtDescricao.setText(perfil.getCodperfil());
            List<PerfilRepositoryHelper> lista = perfilService.setCheckBox(perfil.getTextoMenu());
            this.setListaPerfil(lista);
            this.novoRegistro = false;
        } else {
            novoRegistro = true;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<Perfil> arr = new ArrayList<>();
        arr.addAll(perfilService.findAll());
        if (!novoRegistro) {
            arr.removeIf(p -> p.equals(perfil));
        }
        if (txtDescricao.getText().isEmpty()) {
            msg += "Informe o código do perfil.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(p -> p.getCodperfil().equalsIgnoreCase(txtDescricao.getText()))) {
            msg += "Perfil já cadastrado.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }

    private List<PerfilRepositoryHelper> getListaPerfil() {
        List<PerfilRepositoryHelper> arr = new ArrayList<>();
        arr.add(perfilService.getCheckBox(criarCluster));
        arr.add(perfilService.getCheckBox(criarVm));
        arr.add(perfilService.getCheckBox(criarSo));
        arr.add(perfilService.getCheckBox(criarAmbiente));
        arr.add(perfilService.getCheckBox(criarProgramasvm));
        arr.add(perfilService.getCheckBox(criarProgramas));
        arr.add(perfilService.getCheckBox(criarLinhaSistema));
        arr.add(perfilService.getCheckBox(criarServicos));
        arr.add(perfilService.getCheckBox(criarUsuarios));
        arr.add(perfilService.getCheckBox(criarPerfil));
        arr.add(perfilService.getCheckBox(criarDashbord));
        arr.add(perfilService.getCheckBox(editarCluster));
        arr.add(perfilService.getCheckBox(editarVm));
        arr.add(perfilService.getCheckBox(editarSo));
        arr.add(perfilService.getCheckBox(editarAmbiente));
        arr.add(perfilService.getCheckBox(editarProgramasvm));
        arr.add(perfilService.getCheckBox(editarProgramas));
        arr.add(perfilService.getCheckBox(editarLinhaSistema));
        arr.add(perfilService.getCheckBox(editarServicos));
        arr.add(perfilService.getCheckBox(editarUsuarios));
        arr.add(perfilService.getCheckBox(editarPerfil));
        arr.add(perfilService.getCheckBox(editarDashbord));
        arr.add(perfilService.getCheckBox(excluirCluster));
        arr.add(perfilService.getCheckBox(excluirVm));
        arr.add(perfilService.getCheckBox(excluirSo));
        arr.add(perfilService.getCheckBox(excluirAmbiente));
        arr.add(perfilService.getCheckBox(excluirProgramasvm));
        arr.add(perfilService.getCheckBox(excluirProgramas));
        arr.add(perfilService.getCheckBox(excluirLinhaSistema));
        arr.add(perfilService.getCheckBox(excluirServicos));
        arr.add(perfilService.getCheckBox(excluirUsuarios));
        arr.add(perfilService.getCheckBox(excluirPerfil));
        arr.add(perfilService.getCheckBox(excluirDashbord));
        arr.add(perfilService.getCheckBox(visualizarCluster));
        arr.add(perfilService.getCheckBox(visualizarVm));
        arr.add(perfilService.getCheckBox(visualizarSo));
        arr.add(perfilService.getCheckBox(visualizarAmbiente));
        arr.add(perfilService.getCheckBox(visualizarProgramasvm));
        arr.add(perfilService.getCheckBox(visualizarProgramas));
        arr.add(perfilService.getCheckBox(visualizarLinhaSistema));
        arr.add(perfilService.getCheckBox(visualizarServicos));
        arr.add(perfilService.getCheckBox(visualizarUsuarios));
        arr.add(perfilService.getCheckBox(visualizarPerfil));
        arr.add(perfilService.getCheckBox(visualizarDashbord));
        return arr;
    }

    private void setListaPerfil(List<PerfilRepositoryHelper> lista) {
        for (PerfilRepositoryHelper l : lista) {
            switch (l.getId()) {
                case "criarCluster":
                    this.criarCluster.setSelected(l.getSelecao());
                    break;
                case "criarVm":
                    this.criarVm.setSelected(l.getSelecao());
                    break;
                case "criarSo":
                    this.criarSo.setSelected(l.getSelecao());
                    break;
                case "criarAmbiente":
                    this.criarAmbiente.setSelected(l.getSelecao());
                    break;
                case "criarProgramasvm":
                    this.criarProgramasvm.setSelected(l.getSelecao());
                    break;
                case "criarProgramas":
                    this.criarProgramas.setSelected(l.getSelecao());
                    break;
                case "criarLinhaSistema":
                    this.criarLinhaSistema.setSelected(l.getSelecao());
                    break;
                case "criarServicos":
                    this.criarServicos.setSelected(l.getSelecao());
                    break;
                case "criarUsuarios":
                    this.criarUsuarios.setSelected(l.getSelecao());
                    break;
                case "criarPerfil":
                    this.criarPerfil.setSelected(l.getSelecao());
                    break;
                case "criarDashbord":
                    this.criarDashbord.setSelected(l.getSelecao());
                    break;
                case "editarCluster":
                    this.editarCluster.setSelected(l.getSelecao());
                    break;
                case "editarVm":
                    this.editarVm.setSelected(l.getSelecao());
                    break;
                case "editarSo":
                    this.editarSo.setSelected(l.getSelecao());
                    break;
                case "editarAmbiente":
                    this.editarAmbiente.setSelected(l.getSelecao());
                    break;
                case "editarProgramasvm":
                    this.editarProgramasvm.setSelected(l.getSelecao());
                    break;
                case "editarProgramas":
                    this.editarProgramas.setSelected(l.getSelecao());
                    break;
                case "editarLinhaSistema":
                    this.editarLinhaSistema.setSelected(l.getSelecao());
                    break;
                case "editarServicos":
                    this.editarServicos.setSelected(l.getSelecao());
                    break;
                case "editarUsuarios":
                    this.editarUsuarios.setSelected(l.getSelecao());
                    break;
                case "editarPerfil":
                    this.editarPerfil.setSelected(l.getSelecao());
                    break;
                case "editarDashbord":
                    this.editarDashbord.setSelected(l.getSelecao());
                    break;
                case "excluirCluster":
                    this.excluirCluster.setSelected(l.getSelecao());
                    break;
                case "excluirVm":
                    this.excluirVm.setSelected(l.getSelecao());
                    break;
                case "excluirSo":
                    this.excluirSo.setSelected(l.getSelecao());
                    break;
                case "excluirAmbiente":
                    this.excluirAmbiente.setSelected(l.getSelecao());
                    break;
                case "excluirProgramasvm":
                    this.excluirProgramasvm.setSelected(l.getSelecao());
                    break;
                case "excluirProgramas":
                    this.excluirProgramas.setSelected(l.getSelecao());
                    break;
                case "excluirLinhaSistema":
                    this.excluirLinhaSistema.setSelected(l.getSelecao());
                    break;
                case "excluirServicos":
                    this.excluirServicos.setSelected(l.getSelecao());
                    break;
                case "excluirUsuarios":
                    this.excluirUsuarios.setSelected(l.getSelecao());
                    break;
                case "excluirPerfil":
                    this.excluirPerfil.setSelected(l.getSelecao());
                    break;
                case "excluirDashbord":
                    this.excluirDashbord.setSelected(l.getSelecao());
                    break;
                case "visualizarCluster":
                    this.visualizarCluster.setSelected(l.getSelecao());
                    break;
                case "visualizarVm":
                    this.visualizarVm.setSelected(l.getSelecao());
                    break;
                case "visualizarSo":
                    this.visualizarSo.setSelected(l.getSelecao());
                    break;
                case "visualizarAmbiente":
                    this.visualizarAmbiente.setSelected(l.getSelecao());
                    break;
                case "visualizarProgramasvm":
                    this.visualizarProgramasvm.setSelected(l.getSelecao());
                    break;
                case "visualizarProgramas":
                    this.visualizarProgramas.setSelected(l.getSelecao());
                    break;
                case "visualizarLinhaSistema":
                    this.visualizarLinhaSistema.setSelected(l.getSelecao());
                    break;
                case "visualizarServicos":
                    this.visualizarServicos.setSelected(l.getSelecao());
                    break;
                case "visualizarUsuarios":
                    this.visualizarUsuarios.setSelected(l.getSelecao());
                    break;
                case "visualizarPerfil":
                    this.visualizarPerfil.setSelected(l.getSelecao());
                    break;
                case "visualizarDashbord":
                    this.visualizarDashbord.setSelected(l.getSelecao());
                    break;

            }
        }
    }
}
