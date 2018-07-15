package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.Programa;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.ProgramaService;
import br.com.tbsa.service.UsuarioService;
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

public class ProgramaController implements Initializable {

    private ObservableList<Programa> dados;
    private ProgramaService programaService;
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
    private TableView<Programa> tvPrograma;
    @FXML
    private TableColumn idProgramaCol;
    @FXML
    private TableColumn linhaSistemaCol;
    @FXML
    private TableColumn nomeCol;
    @FXML
    private JFXButton btnAtualizacao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verificationAcess(UsuarioService.list);
        programaService = ProgramaService.getInstancia();
        btnNovo.setTooltip(new Tooltip("Novo"));
        btnAbrir.setTooltip(new Tooltip("Abrir"));
        btnExcluir.setTooltip(new Tooltip("Excluir"));
        btnAtualizacao.setTooltip(new Tooltip("Atualização"));
        this.dados = FXCollections.observableArrayList();

        idProgramaCol.setCellValueFactory(new PropertyValueFactory<>("idPrograma"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        linhaSistemaCol.setCellValueFactory(new PropertyValueFactory<>("linhaSistema"));
        carregarProgramas();
        tvPrograma.setItems(dados);

        FilteredList<Programa> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super Programa>) programa -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(programa.getIdPrograma()).contains(newValue)) {
                        return true;
                    } else if (programa.getNome().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (programa.getLinhaSistema().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Programa> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvPrograma.comparatorProperty());
            tvPrograma.setItems(sortedData);
        });
    }

    @FXML
    private void hendlerNovo(ActionEvent event) throws IOException, Exception {
        novo();
    }

    @FXML
    private void hendlerAbrir(ActionEvent event) throws IOException, Exception {
        editar();
    }

    @FXML
    private void hendlerExcluir(ActionEvent event) {
        excluir();
    }

    @FXML
    private void hendlerAtualizacao(ActionEvent event) {
        carregarProgramasFind();
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

    private void novo() throws IOException, Exception {
        Programa programa = new Programa();
        Boolean btnConfirmarClicked = openWindowEdit(programa);
        Programa newPrograma = new Programa(programa.getLinhaSistema(), programa.getNome());
        if (btnConfirmarClicked) {
            programaService.save(newPrograma);
            HelperNotification.Notification("Programa", "Programa íncluido com sucesso!", NotificationType.SUCCESS);
            carregarProgramas();
        }
    }

    private void editar() throws Exception {
        Programa programa = tvPrograma.getSelectionModel().getSelectedItem();
        if (programa != null) {
            Boolean btnConfirmarClicked = openWindowEdit(programa);
            if (btnConfirmarClicked) {
                programaService.update(programa);
                HelperNotification.Notification("Programa", "Programa editado com sucesso!", NotificationType.SUCCESS);
                carregarProgramas();
            }
        } else {
            HelperNotification.Notification("Programa", "Selecione o programa para edição!", NotificationType.INFORMATION);
        }
    }

    private void excluir() {
        try {
            Programa programa = tvPrograma.getSelectionModel().getSelectedItem();
            if (programa != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir", Alert.AlertType.INFORMATION)) {
                    programaService.delete(programa);
                    dados.remove(programa);
                    HelperNotification.Notification("Programa", "Programa removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Programa", "Selecione o programa para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Programa", e.getMessage(), NotificationType.ERROR);
        }
    }

    private Boolean openWindowEdit(Programa programa) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProgramaEditController.class.getResource("/br/com/tbsa/view/ProgramaEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Cadastro de Programa");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        ProgramaEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setPrograma(programa);

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

    private void carregarProgramas() {
        tvPrograma.getItems().clear();
        List<Programa> arr = null;
        try {
            arr = programaService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Programa", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarProgramasFind() {
        List<Programa> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = programaService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Programa", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void verificationAcess(List<PerfilRepositoryHelper> list) {
        List<PerfilRepositoryHelper> arr = new ArrayList<>();
        arr.addAll(list.parallelStream().filter(l -> l.getId().endsWith("Programas")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarProgramas")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarProgramas")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirProgramas")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }

}
