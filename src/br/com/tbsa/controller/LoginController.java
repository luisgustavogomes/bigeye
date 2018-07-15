package br.com.tbsa.controller;

import br.com.tbsa.App;
import br.com.tbsa.service.UsuarioService;
import br.com.tbsa.utl.HelperNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import tray.notification.NotificationType;

public class LoginController implements Initializable {

    private UsuarioService usuarioService;
    private Stage stage;

    @FXML
    private JFXTextField user;
    @FXML
    private JFXPasswordField pass;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private JFXButton btn_sair;
    @FXML
    private JFXButton btnInformacao;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().add(App.icon);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioService = UsuarioService.getInstancia();
    }

    @FXML
    private void handlerLogin(ActionEvent event) {
        login();
    }

    private void login() {
        try {
            validation();
            if (usuarioService.validationUser(user.getText(), pass.getText())) {
                stage.close();
                openWindow("/br/com/tbsa/view/Home.fxml", "HomeController");
            } else {
                HelperNotification.Notification("Login", "Usuario ou senha ínvalidos!", NotificationType.ERROR);
            }
        } catch (Exception e) {
            HelperNotification.Notification("Login", e.getMessage(), NotificationType.ERROR);
        }
    }

    @FXML
    private void handleSair(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleInformacao(ActionEvent event) {
        information();
    }

    private void information() {
        try {
            openWindow("/br/com/tbsa/view/Information.fxml", "InformationController");
        } catch (Exception e) {
            HelperNotification.Notification("Login", e.getMessage(), NotificationType.ERROR);
        }
    }

    private void openWindow(String path, String controller) throws Exception {
        Stage stage = new Stage();
        stage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(0);
        });
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        if (controller == "InformationController") {
            InformationController ic = loader.<InformationController>getController();
            ic.setStage(stage);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
        } else {
            HomeController hc = loader.<HomeController>getController();
            hc.setStage(stage);
            stage.centerOnScreen();
//            scene.setFill(Color.TRANSPARENT);
//            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("BigEye");
        }
        stage.setScene(scene);
        stage.getIcons().add(App.icon);
        stage.show();
    }

    private void validation() throws Exception {
        String msg = "";
        Boolean control = false;
        if (user.getText().isEmpty()) {
            msg += "Informe o usuario.\n";
            control = true;
        }
        if (pass.getText().isEmpty()) {
            msg += "Informe a senha.\n";
            control = true;
        }
        if (control) {
            throw new Exception(msg);
        }
    }

    @FXML
    private void handlerKeyEntre(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            login();
        }
        if (event.getCode() == KeyCode.F1) {
            information();
        }
        if (event.getCode() == KeyCode.ESCAPE && event.getCode() == KeyCode.SHIFT) {
            System.exit(0);
        }
    }

    @FXML
    private void handlerStage(KeyEvent event) {
    }

}
