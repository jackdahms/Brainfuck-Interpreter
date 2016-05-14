package com.jackdahms;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BrainfuckInterpreter extends Application {
				
	boolean running = true; //is the thread running
	boolean started = false; //is the interpreter running
	
	char[] raw; //the source as a char array
	char[] input; //the input as a char array
	char[] cells = new char[30000]; //the memory of brainfuck, standard size of 30,000 as per wikipedia
	
	Stack<Integer> loops = new Stack<Integer>(); //keeps track of open/close bracket pairs
	LinkedList<String> outputChanges = new LinkedList<String>(); //queue of output
	LinkedList<Integer> memoryIndex = new LinkedList<Integer>(); //queue of displayed memory indices to be updated
	LinkedList<String> memoryChanges = new LinkedList<String>(); //queue of updates to displayed memory
	
	int rawIndex = -1; //the pointer for the source, starts at -1 because it increments before every step
	int inputIndex = 0; //the pointer for the input
	int index = 0; //the pointer for the memory cells
	
	TextField[] cellDisplays; //where the value of the first 20 cells will be displayed;
	TextArea output; //where the output will be printed
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) throws Exception {
		createAndShowGUI(stage);
		
		//runs the actual interpreter
		new Thread(() -> {
			while (running) {
				if (started) {
					step();
				}
			}
		}).start();
		
		//manages updates from interpreter thread to ui thread
		new Thread(() -> {
			while (running) {
				try {
					Thread.sleep(1); //keeps thread from hogging cpu
					Platform.runLater(() -> {
						try {
							cellDisplays[memoryIndex.remove()].setText(memoryChanges.remove());
						} catch (Exception e) {/*do nothing*/}
						//must use separate tries because one failing cannot affect the other
						try {
							output.setText(output.getText() + outputChanges.remove());
						} catch (Exception e) {/*do nothing*/}
					});
				} catch (Exception e) {
					//do nothing
				}
			}
		}).start();
	}	
	
	private void step() {
		if (rawIndex < raw.length - 1) { 
			rawIndex++;
			
			char c = raw[rawIndex];
			
			if (c == '+') cells[index]++;
			else if (c == '-') cells[index]--;
			else if (c == '>') index++;
			else if (c == '<') index--;
			else if (c == '[' && cells[index] > 0) loops.push(rawIndex);
			else if (c == '[' && cells[index] == 0) {
				int open = 1; //count number of open brackets
				do { 
					rawIndex++; 
					if (raw[rawIndex] == '[') open++; //increment for every open bracket
					else if (raw[rawIndex] == ']') open--; //decrement for every close bracket
				} while(raw[rawIndex] != ']' || open > 0); 
			} else if (c == ']' && !loops.isEmpty()) rawIndex = loops.pop() - 1; //must subtract one because the for loop will add one
			else if (c == '.') outputChanges.add("" + cells[index]);
			else if (c == ',') try {cells[index] = input[inputIndex++];} catch (Exception e) {/* do nothing */}
			else step();
			
			if (index < 20) {
				memoryIndex.add(index);
				memoryChanges.add("" + (int)cells[index]);
			}
		} else {
			started = false;
		}
	}
	
	private void createAndShowGUI(Stage stage) {
		int ioHeight = 42; //tall enough for two rows without a scrollbar
		
		stage.setTitle("Brainfuck Interpreter");
		stage.setOnCloseRequest((WindowEvent e) -> {
			running = false;
			stage.close();
		});
        
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 10, 10, 10));
		
		Label inputLabel = new Label("INPUT");
		GridPane.setHgrow(inputLabel, Priority.ALWAYS);
		grid.add(inputLabel, 0, 0);
		
		TextArea input = new TextArea();
		input.setMinHeight(ioHeight);
		grid.add(input, 0, 1);
		
		Label outputLabel = new Label("OUTPUT");
		GridPane.setHgrow(outputLabel, Priority.ALWAYS);
		grid.add(outputLabel, 1, 0);
		
		output = new TextArea();
		output.setMinHeight(ioHeight);
		grid.add(output, 1, 1);
		
		Label memoryLabel = new Label("MEMORY");
		grid.add(memoryLabel, 0, 2);
		
		HBox memory = new HBox(10);
		cellDisplays = new TextField[20];
		for (int i = 0; i < cellDisplays.length; i++) {
			cellDisplays[i] = new TextField("0");
			memory.getChildren().add(cellDisplays[i]);
		}
		grid.add(memory, 0, 3, 2, 1);
		
		Label sourceLabel = new Label("SOURCE");
		grid.add(sourceLabel, 0, 4);
		
		HBox sourceControl = new HBox(10);
		
		TextArea source = new TextArea();
		source.setPrefHeight(10000); //just has to be big enough to reach the bottom of the stage
		HBox.setHgrow(source, Priority.ALWAYS);
		sourceControl.getChildren().add(source);
		
		VBox controls = new VBox(10);
		
		Button[] controlButtons = new Button[7];
		
		controlButtons[0] = new Button("START");
		controlButtons[0].setId("start-button");
		controlButtons[0].setOnAction((ActionEvent e) -> {
			raw = source.getText().toCharArray();
			this.input = input.getText().toCharArray();
			for (int i = 0; i < cellDisplays.length; i++) cells[i] = (char)Integer.parseInt(cellDisplays[i].getText());
			started = true;
		});
		
		controlButtons[1] = new Button("STOP");
		controlButtons[1].setId("stop-button");
		controlButtons[1].setOnAction((ActionEvent e) -> {
			started = false;
		});
		
		controlButtons[2] = new Button("STEP");
		controlButtons[2].setOnAction((ActionEvent e) -> {
			step();
		});
		
		controlButtons[3] = new Button("RESET");
		controlButtons[3].setId("reset-button");
		controlButtons[3].setOnAction((ActionEvent e) -> {
			rawIndex = 0;
			inputIndex = 0;
			index = 0;
			for (int i = 0; i < cellDisplays.length; i++) cellDisplays[i].setText("0");
			for (int i = 0; i < cells.length; i++) cells[i] = 0;
			output.setText("");
		});
		
		controlButtons[4] = new Button("SAVE");
		controlButtons[4].setOnAction((ActionEvent e) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Brainfuck Program");
			fileChooser.setInitialDirectory(new File("src/com/jackdahms/bf"));
			File save = fileChooser.showSaveDialog(stage);
			try {
	            FileWriter fileWriter = new FileWriter(save);
	            fileWriter.write(source.getText());
	            fileWriter.close();
	        } catch (Exception ex) {
	        	System.err.println("Could not save file!");
	        }
		});
		
		controlButtons[5] = new Button("LOAD");
		controlButtons[5].setOnAction((ActionEvent e) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Brainfuck Program");
			fileChooser.setInitialDirectory(new File("src/com/jackdahms/bf"));
			try {
				Scanner load = new Scanner(fileChooser.showOpenDialog(stage));
				load.useDelimiter("\\Z");
				source.setText(load.next());
				load.close();
			} catch (Exception e1) {
				System.err.println("Could not load file!");
			}

			raw = source.getText().toCharArray();
			this.input = input.getText().toCharArray();
			for (int i = 0; i < cellDisplays.length; i++) cells[i] = (char)Integer.parseInt(cellDisplays[i].getText());
		});
		
		controlButtons[6] = new Button("15 STEP");
		controlButtons[6].setId("15step-button");
		controlButtons[6].setOnAction((ActionEvent) -> {
			for (int i = 0; i < 15; i++) step();
		});
		
		int[] order = {0, 1, 2, 6, 3, 4, 5}; //order to add the buttons in
		for (int i = 0; i < controlButtons.length; i++) {
			controlButtons[i].setMaxWidth(Double.MAX_VALUE);
			controls.getChildren().add(controlButtons[order[i]]);
		}
		
		sourceControl.getChildren().add(controls);
		
		grid.add(sourceControl, 0, 5, 2, 1);

		Scene scene = new Scene(grid, 1000, 600);
		try {
			scene.getStylesheets().add(BrainfuckInterpreter.class.getResource("/com/jackdahms/brainfuck.css").toExternalForm());
		} catch (Exception e) {
			System.err.println("Could not load stylesheet...");
		}
		grid.requestFocus(); //takes focus away from input, must be down after grid is added to scene
		stage.setScene(scene);
        stage.show();
	}
	
}