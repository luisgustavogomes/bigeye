package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.ProgramasVmHst;
import br.com.tbsa.service.ProgramasVmHstService;
import br.com.tbsa.utl.HelperAlert;
import br.com.tbsa.utl.HelperDate;
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

public class ProgramasVmHstController implements Initializable {

    private Stage editStage;
    private ObservableList<ProgramasVmHst> dados;
    private ProgramasVmHstService programasVmHstService;
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
    private TableView<ProgramasVmHst> tvProgramasVmHst;
    @FXML
    private TableColumn idHtsCol;
    @FXML
    private TableColumn versaoCol;
    @FXML
    private TableColumn dtatualizacaoCol;
    @FXML
    private TableColumn observacaoCol;
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
        programasVmHstService = ProgramasVmHstService.getInstancia();
        idHtsCol.setCellValueFactory(new PropertyValueFactory<>("idHts"));
        versaoCol.setCellValueFactory(new PropertyValueFactory<>("versao"));
        programasVmCol.setCellValueFactory(new PropertyValueFactory<>("programasVm"));
        dtatualizacaoCol.setCellValueFactory(new PropertyValueFactory<>("dtatualizacao"));
        observacaoCol.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        carregarProgramasVmHsts();
        tvProgramasVmHst.setItems(dados);

        FilteredList<ProgramasVmHst> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super ProgramasVmHst>) programasVmHst -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(programasVmHst.getIdHts()).contains(newValue)) {
                        return true;
                    } else if (programasVmHst.getVersao().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (HelperDate.retornarDataString(programasVmHst.getDtatualizacao()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (programasVmHst.getObservacao().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<ProgramasVmHst> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvProgramasVmHst.comparatorProperty());
            tvProgramasVmHst.setItems(sortedData);
        });
    }

    @FXML
    @SuppressWarnings({"unchecked", "unchecked"})
    private void hendlerNovo(ActionEvent event) throws IOException, Exception {
        novo();
    }

    private void novo() throws IOException, Exception {
        ProgramasVmHst programasVmHst = new ProgramasVmHst();
        Boolean btnConfirmarClicked = openWindowEdit(programasVmHst);
        ProgramasVmHst newProgramasVmHst = new ProgramasVmHst(ProgramasVmController.PROGRAMASVMEDICAO, programasVmHst.getVersao(), programasVmHst.getDtatualizacao(), programasVmHst.getObservacao());
        if (btnConfirmarClicked) {
            programasVmHstService.save(newProgramasVmHst);
            HelperNotification.Notification("Histórico de versão", "Histórico salvo com sucesso!", NotificationType.SUCCESS);
            carregarProgramasVmHsts();
        }
    }

    @FXML
    @SuppressWarnings({"unchecked", "unchecked"})
    private void hendlerAbrir(ActionEvent event) throws Exception {
        editar();
    }

    private void editar() throws Exception {
        ProgramasVmHst programasVmHst = tvProgramasVmHst.getSelectionModel().getSelectedItem();
        if (programasVmHst != null) {
            Boolean btnConfirmarClicked = openWindowEdit(programasVmHst);
            if (btnConfirmarClicked) {
                programasVmHstService.update(programasVmHst);
                HelperNotification.Notification("Histórico", "Histórico editado com sucesso!", NotificationType.SUCCESS);
                carregarProgramasVmHsts();
            }
        } else {
            HelperNotification.Notification("Histórico", "Selecione o histórico para edição!", NotificationType.INFORMATION);
        }
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void hendlerExcluir(ActionEvent event) {
        excluir();
    }

    private void excluir() {
        try {
            ProgramasVmHst programasVmHst = tvProgramasVmHst.getSelectionModel().getSelectedItem();
            if (programasVmHst != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir", Alert.AlertType.INFORMATION)) {
                    programasVmHstService.delete(programasVmHst);
                    dados.remove(programasVmHst);
                    HelperNotification.Notification("Histórico", "Histórico removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Histórico", "Selecione o histórico para remoção", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Histórico", e.getMessage(), NotificationType.ERROR);
        }
    }

    public Stage getEditStage() {
        return editStage;
    }

    public void setEditStage(Stage editStage) {
        this.editStage = editStage;
    }

    private void carregarProgramasVmHsts() {
        tvProgramasVmHst.getItems().clear();
        List<ProgramasVmHst> arr = null;
        try {
            arr = programasVmHstService.findAllWithoutClose(ProgramasVmController.PROGRAMASVMEDICAO.getIdProgramasVm());
        } catch (Exception e) {
            HelperNotification.Notification("Histórico", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarProgramasVmHstsFind() {
        List<ProgramasVmHst> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = programasVmHstService.findAllWithoutClose(ProgramasVmController.PROGRAMASVMEDICAO.getIdProgramasVm());
        } catch (Exception e) {
            HelperNotification.Notification("Histórico", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private Boolean openWindowEdit(ProgramasVmHst programasVmHst) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProgramasVmHstEditController.class.getResource("/br/com/tbsa/view/ProgramasVmHstEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStageOpen = new Stage();
        editStageOpen.getIcons().add(App.icon);
        editStageOpen.centerOnScreen();
        editStageOpen.setTitle("Cadastro de históricos de versão!");
        Scene scene = new Scene(page);
        editStageOpen.setScene(scene);
        editStageOpen.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        ProgramasVmHstEditController controller = loader.getController();
        controller.setEditStage(editStageOpen);
        controller.setProgramasVmHst(programasVmHst);

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
        carregarProgramasVmHstsFind();
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
