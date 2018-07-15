package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.Cluster;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.ClusterService;
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

public class ClusterController implements Initializable {

    private ObservableList<Cluster> dados;
    private ClusterService clusterService;
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
    private TableColumn descricaoCol;
    @FXML
    private TableColumn idClusterCol;
    @FXML
    private TableColumn ipClusterCol;
    @FXML
    private TableColumn soCol;
    @FXML
    private TableView<Cluster> tvCluster;
    @FXML
    private JFXButton btnAtualizacao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verificationAcess(UsuarioService.list);
        clusterService = ClusterService.getInstancia();
        btnNovo.setTooltip(new Tooltip("Novo"));
        btnAbrir.setTooltip(new Tooltip("Abrir"));
        btnExcluir.setTooltip(new Tooltip("Excluir"));
        btnAtualizacao.setTooltip(new Tooltip("Atualização"));
        this.dados = FXCollections.observableArrayList();
        idClusterCol.setCellValueFactory(new PropertyValueFactory<>("idCluster"));
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        ipClusterCol.setCellValueFactory(new PropertyValueFactory<>("ipCluster"));
        soCol.setCellValueFactory(new PropertyValueFactory<>("so"));
        carregarClusters();
        tvCluster.setItems(dados);

        FilteredList<Cluster> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super Cluster>) cluster -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(cluster.getIdCluster()).contains(newValue)) {
                        return true;
                    } else if (cluster.getDescricao().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (cluster.getIpCluster().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Cluster> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvCluster.comparatorProperty());
            tvCluster.setItems(sortedData);
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
    private void hendlerAtualizacao(ActionEvent event) {
        carregarClustersFind();
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

    @FXML
    private void hendlerExcluir(ActionEvent event) {
        excluir();
    }

    private void novo() throws Exception, IOException {
        Cluster cluster = new Cluster();
        Boolean btnConfirmarClicked = openWindowEdit(cluster);
        Cluster newCluster = new Cluster(cluster.getSo(), cluster.getDescricao(), cluster.getIpCluster());
        if (btnConfirmarClicked) {
            clusterService.save(newCluster);
            HelperNotification.Notification("Cluster", "Cluster íncludo com sucesso!", NotificationType.SUCCESS);
            carregarClusters();
        }
    }

    private void editar() throws Exception {
        Cluster cluster = tvCluster.getSelectionModel().getSelectedItem();
        if (cluster != null) {
            Boolean btnConfirmarClicked = openWindowEdit(cluster);
            if (btnConfirmarClicked) {
                clusterService.update(cluster);
                HelperNotification.Notification("Cluster", "Cluster editado com sucesso!", NotificationType.SUCCESS);
                carregarClusters();
            }
        } else {
            HelperNotification.Notification("Cluster", "Selecione o cluster para edição!", NotificationType.INFORMATION);
        }
    }

    private void excluir() {
        try {
            Cluster cluster = tvCluster.getSelectionModel().getSelectedItem();
            if (cluster != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir?", Alert.AlertType.INFORMATION)) {
                    clusterService.delete(cluster);
                    dados.remove(cluster);
                    HelperNotification.Notification("Cluster", "Cluster removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Cluster", "Selecione o cluster para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Cluster", e.getMessage(), NotificationType.ERROR);
        }
    }

    private Boolean openWindowEdit(Cluster cluster) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClusterEditController.class.getResource("/br/com/tbsa/view/ClusterEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Cadastro de Cluster");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        ClusterEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setCluster(cluster);

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

    private void carregarClusters() {
        tvCluster.getItems().clear();
        List<Cluster> arr = null;
        try {
            arr = clusterService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Cluster", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarClustersFind() {
        List<Cluster> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = clusterService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Cluster", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void verificationAcess(List<PerfilRepositoryHelper> list) {
        List<PerfilRepositoryHelper> arr = new ArrayList<>();
        arr.addAll(list.parallelStream().filter(l -> l.getId().contains("Cluster")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarCluster")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarCluster")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirCluster")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }
}
