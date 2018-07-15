package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.Ambiente;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.AmbienteService;
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
import javafx.beans.InvalidationListener;
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

public class AmbienteController implements Initializable {

    private ObservableList<Ambiente> dados;
    private AmbienteService ambienteService;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private JFXButton btnNovo;
    @FXML
    private TableColumn idAmbienteCol;
    @FXML
    private TableColumn descricaoCol;
    @FXML
    private JFXButton btnAbrir;
    @FXML
    private JFXButton btnExcluir;
    @FXML
    private JFXTextField txtPesquisa;
    @FXML
    private TableView<Ambiente> tvAmbiente;
    @FXML
    private JFXButton btnAtualizacao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verificationAcess(UsuarioService.list);
        ambienteService = AmbienteService.getInstancia();
        btnNovo.setTooltip(new Tooltip("Novo"));
        btnAbrir.setTooltip(new Tooltip("Abrir"));
        btnExcluir.setTooltip(new Tooltip("Excluir"));
        btnAtualizacao.setTooltip(new Tooltip("Atualização"));
        this.dados = FXCollections.observableArrayList();

        idAmbienteCol.setCellValueFactory(new PropertyValueFactory<>("idAmbiente"));
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        carregarAmbientes();
        tvAmbiente.setItems(dados);

        FilteredList<Ambiente> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super Ambiente>) ambiente -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(ambiente.getIdAmbiente()).contains(newValue)) {
                        return true;
                    } else if (ambiente.getDescricao().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Ambiente> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvAmbiente.comparatorProperty());
            tvAmbiente.setItems(sortedData);
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
        carregarAmbientesFind();
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
        Ambiente ambiente = new Ambiente();
        Boolean btnConfirmarClicked = openWindowEdit(ambiente);
        Ambiente newAmbiente = new Ambiente(ambiente.getDescricao());
        if (btnConfirmarClicked) {
            ambienteService.save(newAmbiente);
            HelperNotification.Notification("Ambiente", "Ambiente íncluido com sucesso!", NotificationType.SUCCESS);
            carregarAmbientes();
        }
    }

    private void editar() throws Exception {
        Ambiente ambiente = tvAmbiente.getSelectionModel().getSelectedItem();
        if (ambiente != null) {
            Boolean btnConfirmarClicked = openWindowEdit(ambiente);
            if (btnConfirmarClicked) {
                ambienteService.update(ambiente);
                HelperNotification.Notification("Ambiente", "Ambiente editado com sucesso!", NotificationType.SUCCESS);
                carregarAmbientes();
            }
        } else {
            HelperNotification.Notification("Ambiente", "Selecione o ambiente para edição!", NotificationType.INFORMATION);
        }
    }

    private void excluir() {
        try {
            Ambiente ambiente = tvAmbiente.getSelectionModel().getSelectedItem();
            if (ambiente != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir", Alert.AlertType.INFORMATION)) {
                    ambienteService.delete(ambiente);
                    dados.remove(ambiente);
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

    private Boolean openWindowEdit(Ambiente ambiente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AmbienteEditController.class.getResource("/br/com/tbsa/view/AmbienteEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Cadastro de Ambientes");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        AmbienteEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setAmbiente(ambiente);

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

    private void carregarAmbientes() {
        tvAmbiente.getItems().clear();
        List<Ambiente> arr = null;
        try {
            arr = ambienteService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Ambiente", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarAmbientesFind() {
        List<Ambiente> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = ambienteService.findAll();
        } catch (Exception e) {
            HelperNotification.Notification("Ambiente", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void verificationAcess(List<PerfilRepositoryHelper> list) {
        List<PerfilRepositoryHelper> arr = new ArrayList<>();
        arr.addAll(list.parallelStream().filter(l -> l.getId().contains("Ambiente")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarAmbiente")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarAmbiente")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirAmbiente")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }

}
