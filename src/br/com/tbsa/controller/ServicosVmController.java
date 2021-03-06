package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.ServicosVm;
import br.com.tbsa.service.ServicosVmService;
import br.com.tbsa.utl.HelperAlert;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
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

public class ServicosVmController implements Initializable {

    private Stage editStage;
    private ObservableList<ServicosVm> dados;
    private ServicosVmService servicoVmService;
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
    private TableView<ServicosVm> tvServicosVm;
    @FXML
    private TableColumn idServicosVmCol;
    @FXML
    private TableColumn servicoCol;
    @FXML
    private TableColumn programasVmCol;
    @FXML
    private JFXButton btnAtualizacao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnNovo.setTooltip(new Tooltip("Novo"));
        btnAbrir.setTooltip(new Tooltip("Abrir"));
        btnExcluir.setTooltip(new Tooltip("Excluir"));
        btnAtualizacao.setTooltip(new Tooltip("Atualização"));
        this.dados = FXCollections.observableArrayList();
        servicoVmService = ServicosVmService.getInstancia();
        idServicosVmCol.setCellValueFactory(new PropertyValueFactory<>("idServicosVm"));
        programasVmCol.setCellValueFactory(new PropertyValueFactory<>("programasVm"));
        servicoCol.setCellValueFactory(new PropertyValueFactory<>("servico"));
        carregarServicosVm();
        tvServicosVm.setItems(dados);

        FilteredList<ServicosVm> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super ServicosVm>) servicosVm -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(servicosVm.getIdServicosVm()).contains(newValue)) {
                        return true;
                    } else if (servicosVm.getServico().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<ServicosVm> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvServicosVm.comparatorProperty());
            tvServicosVm.setItems(sortedData);
        });
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void hendlerNovo(ActionEvent event) throws IOException, Exception {
        novo();
    }

    private void novo() throws Exception, IOException {
        ServicosVm servicosVm = new ServicosVm();
        Boolean btnConfirmarClicked = openWindowEdit(servicosVm);
        ServicosVm newServicosVm = new ServicosVm(ProgramasVmController.PROGRAMASVMEDICAO, servicosVm.getServico());
        if (btnConfirmarClicked) {
            servicoVmService.save(newServicosVm);
            HelperNotification.Notification("Serviços", "Serviço instalado com sucesso!", NotificationType.SUCCESS);
            carregarServicosVm();
        }
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void hendlerAbrir(ActionEvent event) throws Exception {
        editar();
    }

    private void editar() throws Exception {
        ServicosVm servicosVm = tvServicosVm.getSelectionModel().getSelectedItem();
        if (servicosVm != null) {
            Boolean btnConfirmarClicked = openWindowEdit(servicosVm);
            if (btnConfirmarClicked) {
                servicoVmService.update(servicosVm);
                HelperNotification.Notification("Serviços", "Serviço instalado editado com sucesso!", NotificationType.SUCCESS);
                carregarServicosVm();
            }
        } else {
            HelperNotification.Notification("Serviços", "Selecione o Serviço instalado para edição!", NotificationType.INFORMATION);
        }
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void hendlerExcluir(ActionEvent event) {
        excluir();
    }

    private void excluir() {
        try {
            ServicosVm servicosVm = tvServicosVm.getSelectionModel().getSelectedItem();
            if (servicosVm != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir", Alert.AlertType.INFORMATION)) {
                    servicoVmService.delete(servicosVm);
                    dados.remove(servicosVm);
                    HelperNotification.Notification("Serviços", "Serviço instalado removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Serviços", "Selecione o Serviço instalado para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Serviços", e.getMessage(), NotificationType.ERROR);
        }
    }

    public Stage getEditStage() {
        return editStage;
    }

    public void setEditStage(Stage editStage) {
        this.editStage = editStage;
    }

    @SuppressWarnings("unchecked")
    private void carregarServicosVm() {
        tvServicosVm.getItems().clear();
        List<ServicosVm> arr = null;
        try {
            arr = servicoVmService.findAllWithoutClose(ProgramasVmController.PROGRAMASVMEDICAO.getIdProgramasVm());
        } catch (Exception e) {
            HelperNotification.Notification("Serviços Vm", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarServicosVmFind() {
        List<ServicosVm> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = servicoVmService.findAllWithoutClose(ProgramasVmController.PROGRAMASVMEDICAO.getIdProgramasVm());
        } catch (Exception e) {
            HelperNotification.Notification("Serviços Vm", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private Boolean openWindowEdit(ServicosVm servicosVm) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ServicosVmEditController.class.getResource("/br/com/tbsa/view/ServicosVmEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStageOpen = new Stage();
        editStageOpen.getIcons().add(App.icon);
        editStageOpen.centerOnScreen();
        editStageOpen.setTitle("Cadastro de Servicos");
        Scene scene = new Scene(page);
        editStageOpen.setScene(scene);
        editStageOpen.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        ServicosVmEditController controller = loader.getController();
        controller.setEditStage(editStageOpen);
        controller.setServicosVm(servicosVm);

        page.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        page.setOnMouseDragged((MouseEvent event) -> {
            editStageOpen.setX(event.getScreenX() - xOffset);
            editStageOpen.setY(event.getScreenY() - yOffset);
        });
        editStageOpen.showAndWait();

        return controller.isbtnConfirmarClicked();
    }

    @FXML
    private void hendlerAtualizacao(ActionEvent event) {
        carregarServicosVmFind();
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
}
