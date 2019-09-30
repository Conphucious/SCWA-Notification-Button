package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Email implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String userEmail;
	
	private ArrayList<String> recieverEmails;
	
	private String smtpHost;
	private int smtpPort;
	private String subjectHeader, message;
	
	public Email(String userEmail, ArrayList<String> recieverEmails, ArrayList<String> recieverNumbers, String smtpHost, int smtpPort,
			String subjectHeader, String message) {
		this.userEmail = userEmail;
		this.recieverEmails = recieverEmails;
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.subjectHeader = subjectHeader;
		this.message = message;
	}
	
	public ArrayList<String> getRecieverEmails() {
		return recieverEmails;
	}

	public void setRecieverEmails(ArrayList<String> recieverEmails) {
		this.recieverEmails = recieverEmails;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSubjectHeader() {
		return subjectHeader;
	}

	public void setSubjectHeader(String subjectHeader) {
		this.subjectHeader = subjectHeader;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Email [userEmail=" + userEmail + ", recieverEmails=" + recieverEmails
				 + ", smtpHost=" + smtpHost + ", smtpPort=" + smtpPort + ", subjectHeader="
				+ subjectHeader + ", message=" + message + "]";
	}
	
}
