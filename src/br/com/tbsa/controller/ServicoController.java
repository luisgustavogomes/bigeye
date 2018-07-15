package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.Servico;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.ServicoService;
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

public class ServicoController implements Initializable {

    private ObservableList<Servico> dados;
    private ServicoService servicoService;
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
    private TableView<Servico> tvServico;
    @FXML
    private TableColumn idServicoCol;
    @FXML
    private TableColumn nomeCol;
    @FXML
    private TableColumn statusCol;
    @FXML
    private TableColumn descricaoCol;
    @FXML
    private TableColumn executavelCol;
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
        servicoService = ServicoService.getInstancia();
        idServicoCol.setCellValueFactory(new PropertyValueFactory<>("idServico"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        executavelCol.setCellValueFactory(new PropertyValueFactory<>("executavel"));
        carregarServicos();
        tvServico.setItems(dados);

        FilteredList<Servico> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super Servico>) servico -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    String status = servico.isStatus() ? "true" : "false";
                    if (String.valueOf(servico.getIdServico()).contains(newValue)) {
                        return true;
                    } else if (servico.getNome().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (status.toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (servico.getDescricao().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (servico.getExecutavel().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Servico> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvServico.comparatorProperty());
            tvServico.setItems(sortedData);
        });

    }

    @FXML
    private void hendlerNovo(ActionEvent event) throws IOException, Exception {
        novo();
    }

    private void novo() throws Exception, IOException {
        Servico servico = new Servico();
        Boolean bntConfirmarClicked = openWindowEdit(servico);
        Servico newServico = new Servico(servico.getNome(), servico.isStatus(), servico.getDescricao(), servico.getExecutavel());
        if (bntConfirmarClicked) {
            servicoService.save(newServico);
            HelperNotification.Notification("Serviço", "Serviço íncluido com sucesso!", NotificationType.SUCCESS);
            carregarServicos();
        }
    }

    @FXML
    private void hendlerAbrir(ActionEvent event) throws IOException, Exception {
        editar();
    }

    private void editar() throws Exception {
        Servico servico = tvServico.getSelectionModel().getSelectedItem();
        if (servico != null) {
            Boolean btnConfirmarClicked = openWindowEdit(servico);
            if (btnConfirmarClicked) {
                servicoService.update(servico);
                HelperNotification.Notification("Serviço", "Serviço editado com sucesso!", NotificationType.SUCCESS);
                carregarServicos();
            }
        } else {
            HelperNotification.Notification("Serviço", "Selecione o Serviço para edição!", NotificationType.INFORMATION);
        }
    }

    @FXML
    private void hendlerExcluir(ActionEvent event) {
        excluir();
    }

    private void excluir() {
        try {
            Servico servico = tvServico.getSelectionModel().getSelectedItem();
            if (servico != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir", Alert.AlertType.INFORMATION)) {
                    servicoService.delete(servico);
                    dados.remove(servico);
                    HelperNotification.Notification("Serviço", "Serviço removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Serviço", "Selecione o serviço para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Serviço", e.getMessage(), NotificationType.ERROR);
        }
    }

    private void carregarServicos() {
        tvServico.getItems().clear();
        List<Servico> arr = null;
        try {
            arr = servicoService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Servico", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarServicosFind() {
        List<Servico> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = servicoService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Servico", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private Boolean openWindowEdit(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ServicoEditController.class.getResource("/br/com/tbsa/view/ServicoEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Cadastro de Serviços");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        ServicoEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setServico(servico);

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
        carregarServicosFind();
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
        arr.addAll(list.parallelStream().filter(l -> l.getId().contains("Servicos")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarServicos")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarServicos")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirServicos")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }

}
