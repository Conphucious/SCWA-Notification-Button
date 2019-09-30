package model;

import javax.swing.JOptionPane;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KBListener implements NativeKeyListener {
	
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			try {
				JOptionPane.showMessageDialog(null, "Program terminated", "NT Notifications", JOptionPane.PLAIN_MESSAGE);
				GlobalScreen.unregisterNativeHook();
				System.exit(0);
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
		} else if (e.getKeyCode() == NativeKeyEvent.VC_F12) {
			NTFunctions.alert();
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		
	}

}
