package application;

import java.io.File;

import javax.swing.JOptionPane;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import model.KBListener;

public class NTApplication {

	public static void main(String[] args) {
		
		if (!new File("path.nt").exists()) {
			JOptionPane.showMessageDialog(null, "Error finding configuration file. Please set one in the configuration tool.", "NT Notifications", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new KBListener());
		}
		catch (NativeHookException ex) {
			JOptionPane.showMessageDialog(null, "here was a problem registering the native hook.\n" + ex.getMessage(), "NT Notifications", JOptionPane.ERROR_MESSAGE);
		}
	}

}
