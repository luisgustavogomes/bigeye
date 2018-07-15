package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.LinhaSistema;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.LinhaSistemaService;
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

public class LinhaSistemaController implements Initializable {

    private ObservableList<LinhaSistema> dados;
    private LinhaSistemaService linhaSistemaService;
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
    private TableView<LinhaSistema> tvLinhaSistema;
    @FXML
    private TableColumn idLinhaSistemaCol;
    @FXML
    private TableColumn nomeCol;
    @FXML
    private JFXButton btnAtualizacao;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        verificationAcess(UsuarioService.list);
        linhaSistemaService = LinhaSistemaService.getInstancia();
        btnNovo.setTooltip(new Tooltip("Novo"));
        btnAbrir.setTooltip(new Tooltip("Abrir"));
        btnExcluir.setTooltip(new Tooltip("Excluir"));
        btnAtualizacao.setTooltip(new Tooltip("Atualização"));
        this.dados = FXCollections.observableArrayList();
        idLinhaSistemaCol.setCellValueFactory(new PropertyValueFactory<>("idLinhaSistema"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        carregarLinhaSistemas();
        tvLinhaSistema.setItems(dados);

        FilteredList<LinhaSistema> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super LinhaSistema>) linhaSistema -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(linhaSistema.getIdLinhaSistema()).contains(newValue)) {
                        return true;
                    } else if (linhaSistema.getNome().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<LinhaSistema> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvLinhaSistema.comparatorProperty());
            tvLinhaSistema.setItems(sortedData);
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
        carregarLinhaSistemasFind();
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

    private void novo() throws Exception, IOException {
        LinhaSistema linhaSistema = new LinhaSistema();
        Boolean btnConfirmarClicked = openWindowEdit(linhaSistema);
        LinhaSistema newLinhaSistema = new LinhaSistema(linhaSistema.getNome());
        if (btnConfirmarClicked) {
            linhaSistemaService.save(newLinhaSistema);
            HelperNotification.Notification("Ambiente", "Ambiente íncluido com sucesso!", NotificationType.SUCCESS);
            carregarLinhaSistemas();
        }
    }

    private void editar() throws Exception {
        LinhaSistema linhaSistema = tvLinhaSistema.getSelectionModel().getSelectedItem();
        if (linhaSistema != null) {
            Boolean btnConfirmarClicked = openWindowEdit(linhaSistema);
            if (btnConfirmarClicked) {
                linhaSistemaService.update(linhaSistema);
                HelperNotification.Notification("Ambiente", "Ambiente editado com sucesso!", NotificationType.SUCCESS);
                carregarLinhaSistemas();
            }
        } else {
            HelperNotification.Notification("Ambiente", "Selecione o ambiente para edição!", NotificationType.INFORMATION);
        }
    }

    private void excluir() {
        try {
            LinhaSistema linhaSistema = tvLinhaSistema.getSelectionModel().getSelectedItem();
            if (linhaSistema != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir", Alert.AlertType.INFORMATION)) {
                    linhaSistemaService.delete(linhaSistema);
                    dados.remove(linhaSistema);
                    HelperNotification.Notification("Ambiente", "Ambiente removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Ambiente", "Selecione o ambiente para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Ambiente", e.getMessage(), NotificationType.ERROR);
        }
    }

    private Boolean openWindowEdit(LinhaSistema linhaSistema) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(LinhaSistemaEditController.class.getResource("/br/com/tbsa/view/LinhaSistemaEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Cadastro de Ambientes");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        LinhaSistemaEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setLinhaSistema(linhaSistema);

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

    private void carregarLinhaSistemas() {
        tvLinhaSistema.getItems().clear();
        List<LinhaSistema> arr = null;
        try {
            arr = linhaSistemaService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Linha Sistema", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarLinhaSistemasFind() {
        List<LinhaSistema> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = linhaSistemaService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Linha Sistema", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void verificationAcess(List<PerfilRepositoryHelper> list) {
        List<PerfilRepositoryHelper> arr = new ArrayList<>();
        arr.addAll(list.parallelStream().filter(l -> l.getId().contains("LinhaSistema")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarLinhaSistema")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarLinhaSistema")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirLinhaSistema")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }
}
