/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tbsa.utl;

import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author lg
 */
public class HelperNotification {

    public static void Notification(String title, String msg, NotificationType type) {
        NotificationType notificationType = type;
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(msg);
        tray.setNotificationType(notificationType);
        tray.showAndDismiss(Duration.millis(3000));
    }
}
