package com.jackdahms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

public class BrainfuckInterpreter extends JPanel {
	
	static int FRAME_WIDTH = 1000;
	static int FRAME_HEIGHT = 600;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Cave Generation");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT + 30);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new BrainfuckInterpreter());
		
		frame.setVisible(true);
	}
	
	public BrainfuckInterpreter() {
		createGUI();
	}
	
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		
		g.setColor(Color.white);
//		g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
	}
	
	private void createGUI() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		JTextArea source = new JTextArea(20, 40);
		Font courier = new Font("courier", Font.PLAIN, 20);
		source.setFont(courier);
		source.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		layout.putConstraint(SpringLayout.WEST, source, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, source, 5, SpringLayout.NORTH, this);
		add(source);
	}
	
}
