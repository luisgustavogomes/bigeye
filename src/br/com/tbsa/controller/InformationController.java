package br.com.tbsa.controller;

import br.com.tbsa.App;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InformationController implements Initializable {

    @FXML
    private JFXButton btnVoltar;
    @FXML
    private Label lblSobre;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().add(App.icon);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handlerVoltar(ActionEvent event) {
        stage.close();
    }

}
