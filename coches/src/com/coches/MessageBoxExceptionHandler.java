package com.coches;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.coches.config.ConfigException;
import com.coches.connection.ConnectionException;
import com.coches.data.DataProviderException;

public class MessageBoxExceptionHandler {

	public static void handle(Exception ex) {
		if (ex instanceof ConfigException) {
			JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
			ex.printStackTrace();
			System.exit(-1);
		} else if (ex instanceof ConnectionException || ex instanceof DataProviderException) {		
			JOptionPane.showMessageDialog(null, ex.getMessage());
			ex.printStackTrace();
		} else {
			JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	
}
