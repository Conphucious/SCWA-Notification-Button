package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Email;
import test.MsgBox;

public class NTConfig extends Application {
	
	private BorderPane mainPane;
	private GridPane gp;
	private VBox vbox;
	private Label fileNameLb;
	private TextField senderEmail, smtpHost, smtpPort, recieverEmail, subjectHeader;
	private TextArea messageText;
	private Button openButton, saveButton, setButton, exitButton, testButton;

	private File file;

	private int x = 0, y = 0;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		content();
		actions();
		properties(primaryStage);
	}
	
	private void initialize() {
		mainPane = new BorderPane();
		gp = new GridPane();
		vbox = new VBox(10);
		fileNameLb = new Label("none");
		senderEmail = new TextField();
		smtpHost = new TextField();
		smtpPort = new TextField();
		recieverEmail = new TextField();
		subjectHeader = new TextField();
		messageText = new TextArea();
		testButton = new Button("Test Alert");
		openButton = new Button("Open");
		saveButton = new Button("Save");
		setButton = new Button("Set");
		exitButton = new Button("Exit");
		
		setButton.setDisable(true);
	}

	private void loadData() {
		FileChooser fc = new FileChooser();
		File fcFile = fc.showOpenDialog(null);
		fc.setInitialDirectory(new File("./"));
		if (fcFile != null) {
			try {
				FileInputStream fis = new FileInputStream(fcFile);
				ObjectInputStream ois = new ObjectInputStream(fis);

				Email e = (Email) ois.readObject();

				senderEmail.setText(e.getUserEmail());
				smtpHost.setText(e.getSmtpHost());
				smtpPort.setText(String.valueOf(e.getSmtpPort()));
				
				String emails = "";
				
				for (int i = 0; i < e.getRecieverEmails().size(); i++) {
					emails += e.getRecieverEmails().get(i);
					if (i != e.getRecieverEmails().size() - 1)
						emails += ",";
				}
				
				System.out.println(emails);
				
				
				recieverEmail.setText(emails);
				subjectHeader.setText(e.getSubjectHeader());
				messageText.setText(e.getMessage());

				fis.close();
				ois.close();
				
				file = fcFile;
				
				setButton.setDisable(false);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void content() {
		mainPane.setCenter(vbox);
		vbox.setAlignment(Pos.CENTER);
		addToPane("File: ", fileNameLb);
		addToPane("Sender Email", senderEmail);
		addToPane("SMTP Host", smtpHost);
		addToPane("SMTP Port", smtpPort);
		addToPane("Reciever Email", recieverEmail);
		addToPane("Subject Header", subjectHeader);
		addToPane("Message", messageText);

		messageText.setMaxWidth(200);

		gp.setAlignment(Pos.CENTER);
		gp.setVgap(10);
		gp.setHgap(20);
		vbox.getChildren().add(gp);

		HBox hbox = new HBox(10);
		//testButton
		hbox.getChildren().addAll(openButton, saveButton, setButton, exitButton);
		hbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(hbox);
	}

	private void actions() {
		
		testButton.setOnAction(event -> {
			Email email = checkConfiguration();
			if (email == null)
				MsgBox.alert("Email configuration error.");
			else {
				Properties props = new Properties();
				props.put("mail.smtp.auth", "false");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", email.getSmtpHost());
				props.put("mail.smtp.port", email.getSmtpPort());
			    Session session = Session.getInstance(props);
			    
				try {
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(email.getUserEmail()));
					
					ArrayList<String> emailList = email.getRecieverEmails();
					
					for (String e : emailList) 
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(e));
					
					message.setSubject("Test Connection Alert");
					message.setText("This is a test message alert.");
					Transport.send(message);

					MsgBox.alert("Test Successful!");
				} catch (MessagingException e) {
					MsgBox.alert("Test Unsuccessful. Check network and configuration settings.");
					e.printStackTrace();
				}
			}
		});
		
		openButton.setOnAction(event -> {
			loadData();
			fileNameLb.setText(file.getName());
		});

		saveButton.setOnAction(event -> {
			saveConfiguration();
			fileNameLb.setText(file.getName());
		});

		setButton.setOnAction(event -> {
			if (file == null) {
				MsgBox.alert("No file is opened to set this data");
				return;
			} 
			
			savePath(file);
			MsgBox.alert("File set to: " + file.toString());
		});

		exitButton.setOnAction(event -> {
			Platform.exit();
		});

	}

	private void properties(Stage primaryStage) {
		primaryStage.setScene(new Scene(mainPane, 400, 550));
		primaryStage.setTitle("SCWA Notification - Configuration Tool");
		primaryStage.show();
	}

	private void addToPane(String lb, Node tf) {
		gp.add(new Label(lb), x, y);
		gp.add(tf, x + 1, y++);

		if (tf instanceof TextField)
			((TextField) tf).setMaxWidth(170);
	}

	private Email checkConfiguration() {
		if (!senderEmail.getText().matches("^(.+)@(.+)$") || !recieverEmail.getText().matches("^(.+)@(.+)$")) {
			System.out.println("Error"); // ALERT HERE
			MsgBox.msg("SCWA Notification Configuration Tool", "Email is invalid format.");
		} else if (!smtpPort.getText().matches("(0|[1-9]\\d*)")) {
			MsgBox.msg("SCWA Notification Configuration Tool", "SMTP Port is not valid.");
		} else if (senderEmail.getText().isEmpty()) {
			MsgBox.msg("SCWA Notification Configuration Tool", "One or more fields are empty.");
		} else {

			ArrayList<String> emailList = new ArrayList<>(), numberList = new ArrayList<>();
			
			if (recieverEmail.getText().contains(",")) {
				String[] emails = recieverEmail.getText().trim().split(",");
				
				for (int i = 0; i < emails.length; i++)
					emailList.add(emails[i]);
			} else {
				emailList.add(recieverEmail.getText());
			}
				
			Email e = new Email(senderEmail.getText(), emailList,
					numberList, smtpHost.getText(), Integer.valueOf(smtpPort.getText()),
					subjectHeader.getText(), messageText.getText());
			
			System.out.println(e);
			return e;
		}

		return null;
	}
	
	private void saveConfiguration() {
		Email e = checkConfiguration();

		if (e != null) {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File("./"));
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Notification Tool Files", "*.nt");
			fc.getExtensionFilters().add(extFilter);
			File selectedFile = fc.showSaveDialog(null);

			if (selectedFile == null)
				return;
			
			FileOutputStream fis;
			ObjectOutputStream oos;
			try {
				fis = new FileOutputStream(selectedFile);
				oos = new ObjectOutputStream(fis);
				oos.writeObject(e);
				oos.close();
				file = selectedFile;
				MsgBox.alert("File successfully saved.");
			} catch (IOException e1) {
				MsgBox.alert("Error saving the file.");
				e1.printStackTrace();
			}
			
			setButton.setDisable(false);
		}
	}
	
	private void savePath(File file) {
		try {
			PrintWriter pw = new PrintWriter(new File("path.nt"));
			pw.println(file.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
}
