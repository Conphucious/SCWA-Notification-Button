package test;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Test extends Application {
	
	private static VBox mainPane = new VBox(10);
	private static Text status = new Text("");
	private static TextField email = new TextField("");
	private static Button send = new Button("Send");
	private static int i = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		send.setOnAction(event -> {
			sendNotification();
		});
		
		mainPane.getChildren().addAll(status, email, send);
		
		primaryStage.setScene(new Scene(mainPane, 200, 200));
		primaryStage.show();
	}
	
	public static void sendNotification() {
		String host = "smtp.gmail.com";
		final String user = "@gmail.com";// change accordingly
		final String password = "";// change accordingly

		//String to = "@gmail.com";// change accordingly
		String to = email.getText();

		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");  
	    props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
	    props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		// Compose the message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("STRANGER DANGER :: CREEPY MAN AT FRONT DESK");
			message.setText("I NEED HELP!");

			// send the message
			Transport.send(message);

			status.setText("Email sent! - " + ++i);

		} catch (MessagingException e) {
			status.setText("An error occured - " + ++i);
			e.printStackTrace();
		}
	}

}
