package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.UsuarioService;
import br.com.tbsa.utl.HelperAlert;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import tray.notification.NotificationType;

public class HomeController implements Initializable {

    private Stage stage;

    @FXML
    private TabPane tabPane;
    @FXML
    private JFXButton btnCluster;
    @FXML
    private JFXButton btnPerfil;
    @FXML
    private JFXButton btnUsuario;
    @FXML
    private JFXButton btnVm;
    @FXML
    private JFXButton btnSistemaOperacional;
    @FXML
    private JFXButton btnAmbiente;
    @FXML
    private JFXButton btnProgramasVm;
    @FXML
    private JFXButton btnPrograma;
    @FXML
    private JFXButton btnLinhaSistema;
    @FXML
    private JFXButton btnServico;
    @FXML
    private JFXButton btnSair;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().add(App.icon);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verificationAcess(UsuarioService.list);
    }

    private void openTabPane(String url, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            Parent content = loader.load();
            Tab tab = new Tab(title);
            tab.setContent(content);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        } catch (IOException e) {
            HelperNotification.Notification("Home", e.getMessage(), NotificationType.ERROR);
        }
    }

    @FXML
    private void handlerCluster(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/Cluster.fxml", "Cluster    ");
    }

    @FXML
    private void handlerPerfil(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/Perfil.fxml", "Perfil    ");
    }

    @FXML
    private void handlerUsuario(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/Usuario.fxml", "Usuario    ");
    }

    @FXML
    private void handlerVm(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/Vm.fxml", "VM's    ");
    }

    @FXML
    private void handlerSistemaOperacional(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/So.fxml", "Sistema operacional    ");
    }

    @FXML
    private void handlerAmbiente(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/Ambiente.fxml", "Ambiente    ");
    }

    @FXML
    private void handlerProgramasVm(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/ProgramasVm.fxml", "Programas Instalados    ");
    }

    @FXML
    private void handlerPrograma(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/Programa.fxml", "Programas    ");
    }

    @FXML
    private void handlerLinhaSistemas(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/LinhaSistema.fxml", "Linha Sistemas    ");
    }

    @FXML
    private void handlerServico(ActionEvent event) {
        openTabPane("/br/com/tbsa/view/Servico.fxml", "Serviços    ");
    }

    @FXML
    private void handlerSair(ActionEvent event) {
        if (HelperAlert.confirmarOperacao("Deseja sair?", Alert.AlertType.INFORMATION)) {
            System.exit(0);
        }
    }

    private void verificationAcess(List<PerfilRepositoryHelper> list) {
        List<PerfilRepositoryHelper> arr = new ArrayList<>();
        arr.addAll(list.parallelStream().filter(l -> l.getId().contains("visualizar")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("visualizarCluster")) {
                btnCluster.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarVm")) {
                btnVm.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarSo")) {
                btnSistemaOperacional.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarAmbiente")) {
                btnAmbiente.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarProgramasvm")) {
                btnProgramasVm.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarProgramas")) {
                btnPrograma.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarLinhaSistema")) {
                btnLinhaSistema.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarServicos")) {
                btnServico.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarUsuarios")) {
                btnUsuario.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarPerfil")) {
                btnPerfil.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("visualizarDashbord")) {
                //btnDashboard.setDisable(!lista.getSelecao());
            }
        });

    }

}
