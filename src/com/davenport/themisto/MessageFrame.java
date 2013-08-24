package com.davenport.themisto;
import java.awt.Font;
import java.io.PrintStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;


public class MessageFrame extends JInternalFrame implements EchoTarget {

	private static final long serialVersionUID = 7490222120627680936L;
	private JTextPane textPane = new JTextPane();

	public MessageFrame() {
		textPane.setFont(Font.decode("ARIAL-PLAIN-18"));
		textPane.setVisible(true);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setAutoscrolls(true);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setVisible(true);
		
		this.getContentPane().add(scrollPane);
	}
	
	public void write(String message) {
		textPane.setText(textPane.getText() + message);
	}
}
