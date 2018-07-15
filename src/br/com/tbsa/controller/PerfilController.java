package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.Perfil;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.PerfilService;
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

public class PerfilController implements Initializable {

    private ObservableList<Perfil> dados;
    private PerfilService perfilService;
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
    private TableView<Perfil> tvPerfil;
    @FXML
    private TableColumn idPerfilCol;
    @FXML
    private TableColumn codperfilCol;
    @FXML
    private JFXButton btnAtualizacao;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        verificationAcess(UsuarioService.list);
        perfilService = PerfilService.getInstancia();
        btnNovo.setTooltip(new Tooltip("Novo"));
        btnAbrir.setTooltip(new Tooltip("Abrir"));
        btnExcluir.setTooltip(new Tooltip("Excluir"));
        btnAtualizacao.setTooltip(new Tooltip("Atualização"));
        this.dados = FXCollections.observableArrayList();

        idPerfilCol.setCellValueFactory(new PropertyValueFactory<>("idPerfil"));
        codperfilCol.setCellValueFactory(new PropertyValueFactory<>("codperfil"));
        carregarPerfil();
        tvPerfil.setItems(dados);

        FilteredList<Perfil> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super Perfil>) perfil -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(perfil.getIdPerfil()).contains(newValue)) {
                        return true;
                    } else if (perfil.getCodperfil().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Perfil> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvPerfil.comparatorProperty());
            tvPerfil.setItems(sortedData);
        });

    }

    @FXML
    private void hendlerNovo(ActionEvent event) throws Exception {
        novo();
    }

    @FXML
    private void hendlerAbrir(ActionEvent event) throws Exception {
        editar();
    }

    @FXML
    private void hendlerExcluir(ActionEvent event) {
        excluir();

    }

    @FXML
    private void hendlerAtualizacao(ActionEvent event) {
        carregarPerfilFind();
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
        Perfil perfil = new Perfil();
        Boolean btnConfirmarClicked = openWindowEdit(perfil);
        Perfil newPerfil = new Perfil(perfil.getCodperfil(), perfil.getTextoMenu());
        if (btnConfirmarClicked) {
            perfilService.save(newPerfil);
            HelperNotification.Notification("Perfil", "Perfil íncluido com sucesso!", NotificationType.SUCCESS);
            carregarPerfil();
        }
    }

    private void editar() throws Exception {
        Perfil perfil = tvPerfil.getSelectionModel().getSelectedItem();
        if (perfil != null) {
            Boolean btnConfirmarClicked = openWindowEdit(perfil);
            if (btnConfirmarClicked) {
                perfilService.update(perfil);
                HelperNotification.Notification("Perfil", "Perfil editado com sucesso!", NotificationType.SUCCESS);
                carregarPerfil();
            }
        } else {
            HelperNotification.Notification("Perfil", "Selecione o perfil para edição!", NotificationType.INFORMATION);
        }
    }

    private void excluir() {
        try {
            Perfil perfil = tvPerfil.getSelectionModel().getSelectedItem();
            if (perfil != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir", Alert.AlertType.INFORMATION)) {
                    perfilService.delete(perfil);
                    dados.remove(perfil);
                    HelperNotification.Notification("Perfil", "Perfil removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Perfil", "Selecione o perfil para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Perfil", e.getMessage(), NotificationType.ERROR);
        }
    }

    private Boolean openWindowEdit(Perfil perfil) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PerfilEditController.class.getResource("/br/com/tbsa/view/PerfilEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Cadastro de Perfil");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        PerfilEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setPerfil(perfil);

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

    private void carregarPerfil() {
        tvPerfil.getItems().clear();
        List<Perfil> arr = null;
        try {
            arr = perfilService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Perfil", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarPerfilFind() {
        List<Perfil> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = perfilService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Perfil", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void verificationAcess(List<PerfilRepositoryHelper> list) {
        List<PerfilRepositoryHelper> arr = new ArrayList<>();
        arr.addAll(list.parallelStream().filter(l -> l.getId().contains("Perfil")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarPerfil")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarPerfil")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirPerfil")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }

}
