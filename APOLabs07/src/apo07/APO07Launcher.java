package apo07;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;

class APO07Launcher {
	
	public static void main (String[] args) {
		System.out.println("Hello Mr Doros.");
//		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
//			@Override
	//		public void uncaughtException(Thread t, Throwable e) {
		//		JOptionPane.showMessageDialog(null, e.getMessage());
			//}
//		});
		new MainScreen().setVisible(true);
	}

}
