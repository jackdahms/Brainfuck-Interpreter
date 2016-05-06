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
import javax.swing.JLabel;
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
		
		g.setColor(Color.black);
//		g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
	}
	
	private void createGUI() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		JLabel inputLabel = new JLabel("INPUT");
		JLabel sourceLabel = new JLabel("SOURCE");
		JLabel outputLabel = new JLabel("OUTPUT");
		
		JTextArea input = new JTextArea(1, 40);
		JTextArea source = new JTextArea(18, 40);
		JTextArea output = new JTextArea(1, 40);

		JLabel memoryLabel = new JLabel("MEMORY");
		
		inputLabel.setFont(new Font("courier", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.NORTH, inputLabel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, inputLabel, 5, SpringLayout.WEST, this);
		add(inputLabel);
		
		input.setFont(new Font("courier", Font.PLAIN, 20));
		input.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		input.setTabSize(4);
		layout.putConstraint(SpringLayout.NORTH, input, 5, SpringLayout.SOUTH, inputLabel);
		layout.putConstraint(SpringLayout.WEST, input, 0, SpringLayout.WEST, inputLabel);
		add(input);
		
		sourceLabel.setFont(new Font("courier", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.NORTH, sourceLabel, 5, SpringLayout.SOUTH, input);
		layout.putConstraint(SpringLayout.WEST, sourceLabel, 0, SpringLayout.WEST, inputLabel);
		add(sourceLabel);
		
		source.setFont(new Font("courier", Font.PLAIN, 20));
		source.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		source.setTabSize(4);
		layout.putConstraint(SpringLayout.NORTH, source, 5, SpringLayout.SOUTH, sourceLabel);
		layout.putConstraint(SpringLayout.WEST, source, 0, SpringLayout.WEST, sourceLabel);
		add(source);
		
		outputLabel.setFont(new Font("courier", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.NORTH, outputLabel, 5, SpringLayout.SOUTH, source);
		layout.putConstraint(SpringLayout.WEST, outputLabel, 0, SpringLayout.WEST, inputLabel);
		add(outputLabel);
		
		output.setFont(new Font("courier", Font.PLAIN, 20));
		output.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		output.setTabSize(4);
		layout.putConstraint(SpringLayout.NORTH, output, 5, SpringLayout.SOUTH, outputLabel);
		layout.putConstraint(SpringLayout.WEST, output, 0, SpringLayout.WEST, outputLabel);
		add(output);
		
		layout.putConstraint(SpringLayout.SOUTH, memoryLabel, -5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, memoryLabel, 0, SpringLayout.WEST, this);
		add(memoryLabel);
		
	}
	
}