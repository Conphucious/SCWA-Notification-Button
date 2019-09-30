package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import model.Email;

public class NTShortcut {

	private static Email email;

	public static void main(String[] args) {

		if (!new File("path.nt").exists()) {
			JOptionPane.showMessageDialog(null,"Error finding configuration file. Please set one in the configuration tool.", "NT Notifications", JOptionPane.ERROR_MESSAGE);
			return;
		}

		loadConfiguration();
		sendNotification();
		System.exit(0);
	}

	private static void loadConfiguration() {
		File file = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("path.nt"));
			file = new File(br.readLine());

			System.out.println(file);
			br.close();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileInputStream fis;
		ObjectInputStream ois;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			email = (Email) ois.readObject();

			System.out.println(email);
			
			if (email == null) {
				JOptionPane.showMessageDialog(null, "Invalid configuration file.", "NT Notifications",
						JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}

			fis.close();
			ois.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void sendNotification() {
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
			
			message.setSubject(email.getSubjectHeader());
			message.setText(email.getMessage());
			Transport.send(message);

			JOptionPane.showMessageDialog(null, "Successfully Notified an Emergency Contact.", "NT Notifications",
					JOptionPane.PLAIN_MESSAGE);
		} catch (MessagingException e) {
			JOptionPane.showMessageDialog(null, "An error has occured sending the message.", "NT Notifications",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

}
