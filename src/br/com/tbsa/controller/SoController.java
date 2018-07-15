/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.So;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.SoService;
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
import javafx.event.EventHandler;
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

/**
 * FXML Controller class
 *
 * @author lg
 */
public class SoController implements Initializable {

    private ObservableList<So> dados;
    private SoService soService;
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
    private TableView<So> tvSo;
    @FXML
    private TableColumn idSoCol;
    @FXML
    private TableColumn descricaoCol;
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
        soService = SoService.getInstancia();
        idSoCol.setCellValueFactory(new PropertyValueFactory<>("idSo"));
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        carregarSos();
        tvSo.setItems(dados);

        FilteredList<So> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super So>) so -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(so.getIdSo()).contains(newValue)) {
                        return true;
                    } else if (so.getDescricao().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<So> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvSo.comparatorProperty());
            tvSo.setItems(sortedData);
        });
    }

    @FXML
    private void hendlerNovo(ActionEvent event) throws Exception {
        novo();
    }

    private void novo() throws IOException, Exception {
        So so = new So();
        Boolean btnConfirmarClicked = openWindowEdit(so);
        So newSo = new So(so.getDescricao());
        if (btnConfirmarClicked) {
            soService.save(newSo);
            HelperNotification.Notification("So", "Sistema operacional íncluido com sucesso!", NotificationType.SUCCESS);
            carregarSos();
        }
    }

    @FXML
    private void hendlerAbrir(ActionEvent event) throws Exception {
        editar();
    }

    private void editar() throws Exception {
        So so = tvSo.getSelectionModel().getSelectedItem();
        if (so != null) {
            Boolean btnConfirmarClicked = openWindowEdit(so);
            if (btnConfirmarClicked) {
                soService.update(so);
                HelperNotification.Notification("So", "Sistema operacional editado com sucesso!", NotificationType.SUCCESS);
                carregarSos();
            }
        } else {
            HelperNotification.Notification("So", "Selecione o Sistema operacional para edição!", NotificationType.INFORMATION);
        }
    }

    @FXML
    private void hendlerExcluir(ActionEvent event) {
        excluir();
    }

    private void excluir() {
        try {
            So so = tvSo.getSelectionModel().getSelectedItem();
            if (so != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir", Alert.AlertType.INFORMATION)) {
                    soService.delete(so);
                    dados.remove(so);
                    HelperNotification.Notification("So", "Sistema Operacional removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("So", "Selecione o sistema operacional para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("So", e.getMessage(), NotificationType.ERROR);
        }
    }

    private void carregarSos() {
        tvSo.getItems().clear();
        List<So> arr = null;
        try {
            arr = soService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("So", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarSosFind() {
        List<So> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = soService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("So", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private Boolean openWindowEdit(So so) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SoEditController.class.getResource("/br/com/tbsa/view/SoEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Cadastro de Sistemas Operacionais");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        SoEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setSo(so);

        page.setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        page.setOnMouseDragged(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                editStage.setX(event.getScreenX() - xOffset);
                editStage.setY(event.getScreenY() - yOffset);
            }
        });
        editStage.showAndWait();

        return controller.isbtnConfirmarClicked();
    }

    @FXML
    private void hendlerAtualizacao(ActionEvent event) {
        carregarSosFind();
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
        arr.addAll(list.parallelStream().filter(l -> l.getId().endsWith("So")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarSo")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarSo")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirSo")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }

}
