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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class BrainfuckInterpreter extends JPanel {
	
	static int FRAME_WIDTH = 1000;
	static int FRAME_HEIGHT = 600;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Brainfuck Interpreter");
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

		//cells, well the first twenty of them at least
		
		JLabel inputLabel = new JLabel("INPUT");
		JLabel inputInfoLabel = new JLabel("(ignores white space)");
		JLabel sourceLabel = new JLabel("SOURCE");
		JLabel outputLabel = new JLabel("OUTPUT");
		
		JTextField input = new JTextField();
		JTextArea sourceArea = new JTextArea();
		JScrollPane sourceScroll = new JScrollPane(sourceArea);
		JTextArea output = new JTextArea(2, 40);

		JLabel memoryLabel = new JLabel("MEMORY");
		
		inputLabel.setFont(new Font("courier", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.NORTH, inputLabel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, inputLabel, 5, SpringLayout.WEST, this);
		add(inputLabel);
		
		inputInfoLabel.setFont(new Font("monospace", Font.PLAIN, 12));
		layout.putConstraint(SpringLayout.SOUTH, inputInfoLabel, 0, SpringLayout.SOUTH, inputLabel);
		layout.putConstraint(SpringLayout.WEST, inputInfoLabel, 5, SpringLayout.EAST, inputLabel);
		add(inputInfoLabel);
		
		input.setFont(new Font("courier", Font.PLAIN, 20));
		input.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
		layout.putConstraint(SpringLayout.NORTH, input, 5, SpringLayout.SOUTH, inputLabel);
		layout.putConstraint(SpringLayout.WEST, input, 0, SpringLayout.WEST, inputLabel);
		layout.putConstraint(SpringLayout.EAST, input, -5, SpringLayout.EAST, this);
		add(input);
		
		sourceLabel.setFont(new Font("courier", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.NORTH, sourceLabel, 5, SpringLayout.SOUTH, input);
		layout.putConstraint(SpringLayout.WEST, sourceLabel, 0, SpringLayout.WEST, inputLabel);
		add(sourceLabel);
		
		sourceArea.setFont(new Font("courier", Font.PLAIN, 20));
		sourceArea.setTabSize(4);
		sourceArea.setLineWrap(true);
		sourceScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		layout.putConstraint(SpringLayout.NORTH, sourceScroll, 5, SpringLayout.SOUTH, sourceLabel);
		layout.putConstraint(SpringLayout.WEST, sourceScroll, 0, SpringLayout.WEST, sourceLabel);
		add(sourceScroll);
		
		outputLabel.setFont(new Font("courier", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.NORTH, outputLabel, 5, SpringLayout.SOUTH, sourceScroll);
		layout.putConstraint(SpringLayout.WEST, outputLabel, 0, SpringLayout.WEST, inputLabel);
		add(outputLabel);
		
		output.setFont(new Font("courier", Font.PLAIN, 20));
		output.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
		output.setTabSize(4);
		layout.putConstraint(SpringLayout.NORTH, output, 5, SpringLayout.SOUTH, outputLabel);
		layout.putConstraint(SpringLayout.WEST, output, 0, SpringLayout.WEST, outputLabel);
		add(output);
		
		layout.putConstraint(SpringLayout.SOUTH, memoryLabel, -5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, memoryLabel, 0, SpringLayout.WEST, this);
		add(memoryLabel);
	}
	
}