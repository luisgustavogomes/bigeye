/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tbsa.utl;

import br.com.tbsa.App;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author lg
 */
public class HelperAlert {

    public static void msgDialog(String msg, String title, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(App.icon);
        alert.setContentText(msg);
        alert.show();
    }

    public static Boolean confirmarOperacao(String msg, Alert.AlertType type) {
        Boolean retorno = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(msg);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(App.icon);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            retorno = true;
        }
        return retorno;
    }
}
