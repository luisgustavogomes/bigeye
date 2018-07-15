package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.Vm;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.UsuarioService;
import br.com.tbsa.service.VmService;
import br.com.tbsa.utl.HelperAlert;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tray.notification.NotificationType;

public class VmController implements Initializable {

    private ObservableList<Vm> dados;
    private VmService vmService;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private JFXButton btnNovo;
    @FXML
    private JFXButton btnAbrir;
    @FXML
    private JFXButton btnExcluir;
    @FXML
    private JFXTextField txtPesquisa;
    @FXML
    private TableView<Vm> tvVm;
    @FXML
    private TableColumn idVmCol;
    @FXML
    private TableColumn nomeVmCol;
    @FXML
    private TableColumn ipCol;
    @FXML
    private TableColumn descricaoCol;
    @FXML
    private TableColumn ambienteCol;
    @FXML
    private TableColumn soCol;
    @FXML
    private TableColumn clusterCol;
    @FXML
    private TableColumn servidorBdCol;
    @FXML
    private JFXButton btnAtualizacao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verificationAcess(UsuarioService.list);
        btnNovo.setTooltip(new Tooltip("Novo"));
        btnAbrir.setTooltip(new Tooltip("Abrir"));
        btnExcluir.setTooltip(new Tooltip("Excluir"));
        btnAtualizacao.setTooltip(new Tooltip("Atualização"));
        this.dados = FXCollections.observableArrayList();
        vmService = VmService.getInstancia();
        idVmCol.setCellValueFactory(new PropertyValueFactory<>("idVm"));
        nomeVmCol.setCellValueFactory(new PropertyValueFactory<>("nomeVm"));
        ipCol.setCellValueFactory(new PropertyValueFactory<>("ipVm"));
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        ambienteCol.setCellValueFactory(new PropertyValueFactory<>("ambiente"));
        clusterCol.setCellValueFactory(new PropertyValueFactory<>("cluster"));
        soCol.setCellValueFactory(new PropertyValueFactory<>("so"));
        servidorBdCol.setCellValueFactory(new PropertyValueFactory<>("servidorBd"));
        carregarVms();
        tvVm.setItems(dados);

        FilteredList<Vm> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super Vm>) vm -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(vm.getIdVm()).contains(newValue)) {
                        return true;
                    } else if (vm.getNomeVm().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (vm.getIpVm().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (vm.getDescricao().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (vm.getAmbiente().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (vm.getSo().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (vm.getCluster().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Vm> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvVm.comparatorProperty());
            tvVm.setItems(sortedData);
        }
        );

    }

    @FXML
    private void hendlerNovo(ActionEvent event) throws IOException, Exception {
        novo();
    }

    private void novo() throws Exception, IOException {
        Vm vm = new Vm();
        Boolean btnConfirmarClicked = openWindowEdit(vm);
        Vm newVm = new Vm(vm.getAmbiente(), vm.getCluster(), vm.getSo(), vm.getNomeVm(), vm.getIpVm(), vm.getDescricao(), vm.isServidorBd());
        if (btnConfirmarClicked) {
            vmService.save(vm);
            HelperNotification.Notification("Vm", "Vm íncludo com sucesso!", NotificationType.SUCCESS);
            carregarVms();
        }
    }

    @FXML
    private void hendlerAbrir(ActionEvent event) throws Exception, IOException {
        editar();
    }

    private void editar() throws Exception {
        Vm vm = tvVm.getSelectionModel().getSelectedItem();
        if (vm != null) {
            Boolean btnConfirmarClicked = openWindowEdit(vm);
            if (btnConfirmarClicked) {
                vmService.update(vm);
                HelperNotification.Notification("Vm", "Vm editado com sucesso!", NotificationType.SUCCESS);
                carregarVms();
            }
        } else {
            HelperNotification.Notification("Vm", "Selecione o vm para edição!", NotificationType.INFORMATION);
        }
    }

    @FXML
    private void hendlerExcluir(ActionEvent event) {
        excluir();
    }

    private void excluir() {
        try {
            Vm vm = tvVm.getSelectionModel().getSelectedItem();
            if (vm != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir?", Alert.AlertType.INFORMATION)) {
                    vmService.delete(vm);
                    dados.remove(vm);
                    HelperNotification.Notification("Vm", "Vm removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Vm", "Selecione o vm para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Vm", e.getMessage(), NotificationType.ERROR);
        }
    }

    private void carregarVms() {
        tvVm.getItems().clear();
        List<Vm> arr = null;
        try {
            arr = vmService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Vm", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarVmsFind() {
        List<Vm> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = vmService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Vm", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private Boolean openWindowEdit(Vm vm) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VmEditController.class.getResource("/br/com/tbsa/view/VmEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Cadastro de Vm");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        VmEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setVm(vm);

        page.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        page.setOnMouseDragged((MouseEvent event) -> {
            editStage.setX(event.getScreenX() - xOffset);
            editStage.setY(event.getScreenY() - yOffset);
        });
        editStage.showAndWait();

        return controller.isbtnConfirmarClicked();
    }

    @FXML
    private void hendlerAtualizacao(ActionEvent event) {
        carregarVmsFind();
    }

    @FXML
    private void handlerKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            txtPesquisa.clear();
        }
    }

    @FXML
    private void handlerEdicao(MouseEvent event) throws Exception {
        if (event.getClickCount() == 2) {
            editar();
        }
    }

    private void verificationAcess(List<PerfilRepositoryHelper> list) {
        List<PerfilRepositoryHelper> arr = new ArrayList<>();
        arr.addAll(list.parallelStream().filter(l -> l.getId().endsWith("rVm")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarVm")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarVm")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirVm")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }
}
