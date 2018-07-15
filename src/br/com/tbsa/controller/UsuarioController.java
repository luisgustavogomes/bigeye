package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.entity.Usuario;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.service.UsuarioService;
import br.com.tbsa.utl.HelperAlert;
import br.com.tbsa.utl.HelperNotification;
import br.com.tbsa.utl.HelperString;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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

public class UsuarioController implements Initializable {

    private ObservableList<Usuario> dados;
    private UsuarioService usuarioService;
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
    private TableView<Usuario> tvUsuario;
    @FXML
    private TableColumn idUsuarioCol;
    @FXML
    private TableColumn codusuarioCol;
    @FXML
    private TableColumn nomeCol;
    @FXML
    private TableColumn perfilCol;
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
        usuarioService = UsuarioService.getInstancia();
        idUsuarioCol.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        codusuarioCol.setCellValueFactory(new PropertyValueFactory<>("codusuario"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        perfilCol.setCellValueFactory(new PropertyValueFactory<>("perfil"));
        carregarUsuarios();
        tvUsuario.setItems(dados);

        FilteredList<Usuario> filter = new FilteredList<>(dados, e -> true);
        txtPesquisa.setOnKeyReleased(e -> {
            txtPesquisa.textProperty().addListener((ObservableList, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super Usuario>) usuario -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(usuario.getIdUsuario()).contains(newValue)) {
                        return true;
                    } else if (usuario.getPerfil().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (usuario.getCodusuario().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (usuario.getNome().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Usuario> sortedData = new SortedList<>(filter);
            sortedData.comparatorProperty().bind(tvUsuario.comparatorProperty());
            tvUsuario.setItems(sortedData);
        });

    }

    @FXML
    private void hendlerNovo(ActionEvent event) throws Exception {
        novo();
    }

    private void novo() throws Exception {
        Usuario usuario = new Usuario();
        Boolean btnConfirmarClicked = openWindowEdit(usuario);
        Usuario newUsuario = new Usuario(usuario.getPerfil(), usuario.getCodusuario(), usuario.getNome(), HelperString.cript(usuario.getSenha()));
        if (btnConfirmarClicked) {
            usuarioService.save(newUsuario);
            HelperNotification.Notification("Usuario", "Usuario íncludo com sucesso!", NotificationType.SUCCESS);
            carregarUsuarios();
        }
    }

    @FXML
    private void hendlerAbrir(ActionEvent event) throws Exception {
        editar();
    }

    private void editar() throws Exception {
        Usuario usuario = tvUsuario.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            Boolean btnConfirmarClicked = openWindowEdit(usuario);
            if (btnConfirmarClicked) {
                usuario.setSenha(HelperString.cript(usuario.getSenha()));
                usuarioService.update(usuario);
                HelperNotification.Notification("Usuario", "Usuario editado com sucesso!", NotificationType.SUCCESS);
                carregarUsuarios();
            }
        } else {
            HelperNotification.Notification("Usuario", "Selecione o usuario para edição!", NotificationType.INFORMATION);
        }
    }

    @FXML
    private void hendlerExcluir(ActionEvent event) {
        excluir();
    }

    private void excluir() {
        try {
            Usuario usuario = tvUsuario.getSelectionModel().getSelectedItem();
            if (usuario != null) {
                if (HelperAlert.confirmarOperacao("Deseja excluir?", Alert.AlertType.INFORMATION)) {
                    usuarioService.delete(usuario);
                    dados.remove(usuario);
                    HelperNotification.Notification("Usuario", "Usuario removido com sucesso!", NotificationType.SUCCESS);
                    return;
                }
                return;
            }
            HelperNotification.Notification("Usuario", "Selecione o usuario para exclusão", NotificationType.INFORMATION);
        } catch (Exception e) {
            HelperNotification.Notification("Usuario", e.getMessage(), NotificationType.ERROR);
        }
    }

    private void carregarUsuarios() {
        tvUsuario.getItems().clear();
        List<Usuario> arr = null;
        try {
            arr = usuarioService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Usuário", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private void carregarUsuariosFind() {
        List<Usuario> arr = null;
        txtPesquisa.clear();
        try {
            dados.clear();
            arr = usuarioService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Usuário", e.getMessage(), NotificationType.ERROR);
        }
        dados.addAll(arr);
    }

    private Boolean openWindowEdit(Usuario usuario) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UsuarioEditController.class.getResource("/br/com/tbsa/view/UsuarioEdit.fxml"));
        VBox page = (VBox) loader.load();
        Stage editStage = new Stage();
        editStage.getIcons().add(App.icon);
        editStage.centerOnScreen();
        editStage.setTitle("Cadastro de Usuario");
        Scene scene = new Scene(page);
        editStage.setScene(scene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        UsuarioEditController controller = loader.getController();
        controller.setEditStage(editStage);
        controller.setUsuario(usuario);

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
        carregarUsuariosFind();
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
        arr.addAll(list.parallelStream().filter(l -> l.getId().endsWith("Usuarios")).collect(Collectors.toList()));
        arr.forEach((lista) -> {
            if (lista.getId().contains("criarUsuarios")) {
                btnNovo.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("editarUsuarios")) {
                btnAbrir.setDisable(!lista.getSelecao());
            } else if (lista.getId().contains("excluirUsuarios")) {
                btnExcluir.setDisable(!lista.getSelecao());
            }
        });
    }
}
