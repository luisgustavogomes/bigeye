package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.ProgramasVm;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.ProgramasVmService;
import br.com.tbsa.service.UsuarioService;
import br.com.tbsa.utl.HelperAlert;
import br.com.tbsa.utl.HelperDate;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tray.notification.NotificationType;

public class ProgramasVmController implements Initializable {

    protected static ProgramasVm PROGRAMASVMEDICAO;

    private ObservableList<ProgramasVm> dados;
    private ProgramasVmService programasVmService;
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
    private TableView<ProgramasVm> tvProgramasVm;
    @FXML
    private TableColumn idProgramasVmCol;
    @FXML
    private TableColumn programaCol;
    @FXML
    private TableColumn vmCol;
    @FXML
    private TableColumn dataInstalacaoCol;
    @FXML
    private JFXButton btnServico;
    @FXML
    private JFXButton btnHistorico;
    @FXML
    private JFXButton btnAtualizacao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verificationAcess(UsuarioService.list);
        btnNovo.setTooltip(new Tooltip("Novo"));
        btnAbrir.setTooltip(new Tooltip("Abrir"));
        btnExcluir.setTooltip(new Tooltip("Excluir"));
        btnServico.setTooltip(new Tooltip("Serviços"));
        btnHistorico.setTooltip(new Tooltip("Histórico"));
        btnAtualizacao.setTooltip(new Tooltip("Atualização"));
        this.dados = FXCollections.observableArrayList();
        programasVmService = ProgramasVmService.getInstancia();
        idProgramasVmCol.setCellValueFactory(new PropertyValueFactory<>("idProgramasVm"));
        programaCol.setCellValueFactory(new PropertyValueFactory<>("programa"));
        vmCol.setCellValueFactory(new PropertyValueFactory<>("vm"));
        dataInstalacaoCol.setCellValueFactory(new PropertyValueFactory<>("dataInstalacao"));
        carregarProgramasVms();
        tvProgramasVm.setItems(dados);

        FilteredList<ProgramasVm> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super ProgramasVm>) programasVm -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(programasVm.getIdProgramasVm()).contains(newValue)) {
                        return true;
                    } else if (programasVm.getPrograma().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (programasVm.getVm().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (HelperDate.retornarDataString(programasVm.getDataInstalacao()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<ProgramasVm> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvProgramasVm.comparatorProperty());
            tvProgramasVm.setItems(sortedData);
        });
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void hendlerNovo(ActionEvent event) throws IOException, Exception {
        novo();
    }

    private void novo() throws IOException, Exception {
        ProgramasVm programasVm = new ProgramasVm();
        Boolean btnConfirmarClicked = openWindowEdit(programasVm);
        ProgramasVm newProgramasVm = new ProgramasVm(programasVm.getPrograma(), programasVm.getVm(), programasVm.getDataInstalacao());
        if (btnConfirmarClicked) {
            programasVmService.save(programasVm);
            HelperNotification.Notification("Programas Instalados", "Programas instalado íncludo com sucesso!", NotificationType.SUCCESS);
            carregarProgramasVms();
        }
    }

    @FXML
    private void hendlerAbrir(ActionEvent event) throws IOException, Exception {
        editar();
    }

    private void editar() throws Exception {
        ProgramasVm programasVm = tvProgramasVm.getSelectionModel().getSelectedItem();
        if (programasVm != null) {
            Boolean btnConfirmarClicked = openWindowEdit(programasVm);
            if (btnConfirmarClicked) {
                programasVmService.update(programasVm);
                HelperNotification.Notification("Programas Instalados", "Programas instalado editado com sucesso!", NotificationType.SUCCESS);
                carregarProgramasVms();
            }
        } else {
            HelperNotification.Notification("Programas Instalados", "Selecione o programas instalados para edição!", NotificationType.INFORMATION);
        }
    }

    @FXML
    private void hendlerExcluir(ActionEvent event) {
        excluir();
    }

    private void excluir() {
        try {
            ProgramasVm programasVm = tvProgramasVm.getSelectionModel().getSelectedItem();
            if (programasVm != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir?", Alert.AlertType.INFORMATION)) {
                    programasVmService.delete(programasVm);
                    dados.remove(programasVm);
                    HelperNotification.Notification("Programas Instalados", "Programas instalado removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Programas Instalados", "Selecione o programas instalados para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Programas Instalados", e.getMessage(), NotificationType.ERROR);
        }
    }

    private void carregarProgramasVms() {
        tvProgramasVm.getItems().clear();
        List<ProgramasVm> arr = null;
        try {
            arr = programasVmService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Programas Instalados", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarProgramasVmsFind() {
        List<ProgramasVm> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = programasVmService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Programas Instalados", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private Boolean openWindowEdit(ProgramasVm programasVm) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProgramasVmEditController.class.getResource("/br/com/tbsa/view/ProgramasVmEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Programas Instalados");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        ProgramasVmEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setProgramasVm(programasVm);

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
    private void hendlerServico(ActionEvent event) {
        try {
            PROGRAMASVMEDICAO = tvProgramasVm.getSelectionModel().getSelectedItem();
            if (PROGRAMASVMEDICAO != null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(ServicosVmController.class.getResource("/br/com/tbsa/view/ServicosVm.fxml"));
                VBox page = (VBox) loader.load();
                Stage editStage = new Stage();
                editStage.getIcons().add(App.icon);
                editStage.centerOnScreen();
                editStage.setTitle("Serviços Instalados");
                Scene scene = new Scene(page);
                editStage.setScene(scene);
                ServicosVmController controller = loader.getController();
                controller.setEditStage(editStage);
                editStage.initModality(Modality.WINDOW_MODAL);
                editStage.showAndWait();
                PROGRAMASVMEDICAO = null;
                return;
            }
            HelperNotification.Notification("Programas Instalados", "Selecione o programa para exibição dos serviços!", NotificationType.INFORMATION);
        } catch (IOException e) {
            HelperNotification.Notification("Programas Instalados", e.getMessage(), NotificationType.ERROR);
        }
    }

    @FXML
    private void hendlerHistorico(ActionEvent event) {
        try {
            PROGRAMASVMEDICAO = tvProgramasVm.getSelectionModel().getSelectedItem();
            if (PROGRAMASVMEDICAO != null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(ProgramasVmHstController.class.getResource("/br/com/tbsa/view/ProgramasVmHst.fxml"));
                VBox page = (VBox) loader.load();
                Stage editStage = new Stage();
                editStage.getIcons().add(App.icon);
                editStage.centerOnScreen();
                editStage.setTitle("Histórico");
                Scene scene = new Scene(page);
                editStage.setScene(scene);
                ProgramasVmHstController controller = loader.getController();
                controller.setEditStage(editStage);
                editStage.initModality(Modality.WINDOW_MODAL);
                editStage.showAndWait();
                PROGRAMASVMEDICAO = null;
                return;
            }
            HelperNotification.Notification("Programas Instalados", "Selecione o programa para exibição dos serviços!", NotificationType.INFORMATION);
        } catch (IOException e) {
            HelperNotification.Notification("Programas Instalados", e.getMessage(), NotificationType.ERROR);
        }
    }

    @FXML
    private void hendlerAtualizacao(ActionEvent event) {
        carregarProgramasVmsFind();
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
        arr.addAll(list.parallelStream().filter(l -> l.getId().endsWith("Programasvm")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarProgramasvm")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarProgramasvm")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirProgramasvm")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }

}
