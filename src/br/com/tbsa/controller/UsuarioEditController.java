package br.com.tbsa.controller;

import br.com.tbsa.entity.Perfil;
import br.com.tbsa.entity.Usuario;
import br.com.tbsa.service.PerfilService;
import br.com.tbsa.service.UsuarioService;
import br.com.tbsa.utl.ComboBoxAutoComplete;
import br.com.tbsa.utl.HelperNotification;
import br.com.tbsa.utl.HelperString;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import tray.notification.NotificationType;

public class UsuarioEditController implements Initializable {

    private Stage editStage;
    private Boolean btnConfirmarClicked = false;
    private Usuario usuario;
    private UsuarioService usuarioService;
    private PerfilService perfilService;
    private Boolean novoRegistro;

    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXComboBox<Perfil> cbPerfil;
    @FXML
    private JFXTextField txtNome;
    @FXML
    private JFXTextField txtCodUsuario;
    @FXML
    private JFXPasswordField txtSenha;
    @FXML
    private JFXPasswordField txtConfirmarSenha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        perfilService = PerfilService.getInstancia();
        usuarioService = UsuarioService.getInstancia();
        cbPerfil.getItems().addAll(carregarComboPerfil());
        new ComboBoxAutoComplete<Perfil>(cbPerfil);
    }

    @FXML
    private void handlerConfirmar(ActionEvent event) {
        try {
            validation();
            usuario.setNome(txtNome.getText());
            usuario.setCodusuario(txtCodUsuario.getText());
            usuario.setPerfil(cbPerfil.getSelectionModel().getSelectedItem());
            usuario.setSenha(txtSenha.getText());
            btnConfirmarClicked = true;
            editStage.close();
        } catch (Exception e) {
            HelperNotification.Notification("Usuario", e.getMessage(), NotificationType.ERROR);
        }
    }

    @FXML
    private void handlerCancelar(ActionEvent event) {
        editStage.close();
    }

    public Boolean isbtnConfirmarClicked() {
        return btnConfirmarClicked;
    }

    public Stage getEditStage() {
        return editStage;
    }

    public void setEditStage(Stage editStage) {
        this.editStage = editStage;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) throws Exception {
        this.usuario = usuario;
        if (usuario.getCodusuario() != null) {
            this.txtNome.setText(usuario.getNome());
            this.txtCodUsuario.setText(usuario.getCodusuario());
            JFXComboBox<Perfil> box = null;
            for (int i = 0; i < cbPerfil.getItems().size(); i++) {
                if (cbPerfil.getItems().get(i).toString()
                        .equalsIgnoreCase(usuario.getPerfil().getCodperfil())) {
                    this.cbPerfil.getSelectionModel().select(i);
                    break;
                }
            }
            UsuarioService service = UsuarioService.getInstancia();
            String senha = service.findByCod(usuario.getIdUsuario()).getSenha();
            this.txtSenha.setText(HelperString.decript(senha));
            this.txtConfirmarSenha.setText(HelperString.decript(senha));
            this.novoRegistro = false;
        } else {
            this.novoRegistro = true;
        }
    }

    private List<Perfil> carregarComboPerfil() {
        try {
            return perfilService.findAllWithoutClose();
        } catch (Exception e) {
            HelperNotification.Notification("Usuario", e.getMessage(), NotificationType.ERROR);
            return null;
        }
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        List<Usuario> arr = new ArrayList<>();
        arr.addAll(usuarioService.findAllWithoutClose());
        if (!novoRegistro) {
            arr.removeIf(u -> u.equals(usuario));
        }
        if (txtNome.getText().isEmpty()) {
            msg += "Informe um nome.\n";
            control = true;
        }
        if (txtCodUsuario.getText().isEmpty()) {
            msg += "Informe um código de usuário.\n";
            control = true;
        }
        if (cbPerfil.getSelectionModel().getSelectedItem() == null) {
            msg += "Escolha um perfil.\n";
            control = true;
        }
        if (txtSenha.getText().isEmpty()) {
            msg += "Informe um senha.\n";
            control = true;
        }
        if (txtConfirmarSenha.getText().isEmpty()) {
            msg += "Confirme a senha.\n";
            control = true;
        }
        if (txtConfirmarSenha.getText() == txtSenha.getText()) {
            msg += "Senhas não são idênticas.\n";
            control = true;
        }
        if (arr.parallelStream().anyMatch(u -> u.getCodusuario().equalsIgnoreCase(txtCodUsuario.getText()))) {
            msg = "Código de usuário já incluso.";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }

}
