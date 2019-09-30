package model;

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

public class NTFunctions {
	
	private static Email email;
	public static int count = 0;
	
	public static void alert() {
		email = loadConfiguration();
		if (email == null) {
			JOptionPane.showMessageDialog(null, "Email configuration error.", "NT Notifications", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} 
		
		sendNotification();
	}
	
	private static Email loadConfiguration() {
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
			
			fis.close();
			ois.close();
			
			return email;
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private static void sendNotification() {
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

			JOptionPane.showMessageDialog(null, "Successfully Notified an Emergency Contact " + (++count), "NT Notifications", JOptionPane.PLAIN_MESSAGE);
		} catch (MessagingException e) {
			JOptionPane.showMessageDialog(null, "An error occured during notification.", "NT Notifications", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

}
