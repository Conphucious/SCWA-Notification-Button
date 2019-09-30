package test;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MsgBox {

	public static void msg(String title, String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	public static void alert(String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("SCWA Notification - Configuration Tool");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

}
