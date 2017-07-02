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
	
	boolean threadRunning = true; //is the thread running
	volatile boolean intialized = false; //is the interpreter ready to run
	boolean programRunning = false; //is the interpreter started
	
	char[] program; //the source as a char array
	char[] input; //the input as a char array
	char[] cells = new char[30000]; //the memory of brainfuck, standard size of 30,000 as per wikipedia
	
	Stack<Integer> loops = new Stack<Integer>(); //keeps track of open/close bracket pairs
	LinkedList<String> outputChanges = new LinkedList<String>(); //queue of output
	LinkedList<Integer> memoryIndices = new LinkedList<Integer>(); //queue of displayed memory indices to be updated
	LinkedList<String> memoryChanges = new LinkedList<String>(); //queue of updates to displayed memory
	
	int stepIndex = -1; //the pointer for the source, starts at -1 because it increments before every step
	int inputIndex = 0; //the pointer for the input
	int memoryIndex = 0; //the pointer for the memory cells
	
	TextField[] cellDisplays; //where the value of the first 20 cells will be displayed;
	TextArea sourceArea; //where the program is typed
	TextArea inputArea; //where the input is typed
	TextArea outputArea; //where the output will be printed
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) {
		createAndShowGUI(stage);
		
		//runs the actual interpreter
		new Thread(() -> {
			while (threadRunning) {
				try {
					Thread.sleep(1); //keeps thread from hogging cpu 
				} catch (InterruptedException e) {/* do nothing */}
				if (intialized && programRunning) {
					step();
				}
			}
		}).start();
		
		//manages updates from interpreter thread to ui thread
		new Thread(() -> {
			while (threadRunning) {
				try {
					Thread.sleep(1); //keeps thread from hogging cpu
					Platform.runLater(() -> {
						try {
							cellDisplays[memoryIndices.remove()].setText(memoryChanges.remove());
						} catch (Exception e) {/*do nothing*/}
						//must use separate tries because one failing cannot affect the other
						try {
							outputArea.setText(outputArea.getText() + outputChanges.remove());
						} catch (Exception e) {/*do nothing*/}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}	
	
	private void initialize() {
		program = sourceArea.getText().toCharArray();
		input = inputArea.getText().toCharArray();
		for (int i = 0; i < cellDisplays.length; i++) {
			cells[i] = (char)Integer.parseInt(cellDisplays[i].getText());
		}
		intialized = true;
	}
	
	private void step() {
		if (!intialized) {
			initialize();
		}
		if (stepIndex < program.length - 1) { 
			stepIndex++;
			
			char c = program[stepIndex];
			
			if (c == '+') {
				cells[memoryIndex]++;
			} else if (c == '-') {
				cells[memoryIndex]--;
			} else if (c == '>') {
				memoryIndex++;
			} else if (c == '<') {
				memoryIndex--;
			} else if (c == '[' && cells[memoryIndex] > 0) {
				loops.push(stepIndex);
			} else if (c == '[' && cells[memoryIndex] == 0) {
				int open = 1; //count number of open brackets
				do { 
					stepIndex++; 
					if (program[stepIndex] == '[') open++; //increment for every open bracket
					else if (program[stepIndex] == ']') open--; //decrement for every close bracket
				} while(program[stepIndex] != ']' || open > 0); 
			} else if (c == ']' && !loops.isEmpty()) {
				stepIndex = loops.pop() - 1; //must subtract one because the for loop will add one
			} else if (c == '.') {
				outputChanges.add("" + cells[memoryIndex]);
			} else if (c == ',') {
				try {cells[memoryIndex] = input[inputIndex++];} catch (Exception e) {/* do nothing */}
			} else {
				step();
			}
			
			if (memoryIndex < 20) {
				memoryIndices.add(memoryIndex);
				memoryChanges.add("" + (int)cells[memoryIndex]);
			}
		} else {
			programRunning = false;
		}
	}
	
	private void reset() {
		stepIndex = -1;
		inputIndex = 0;
		memoryIndex = 0;
		for (int i = 0; i < cellDisplays.length; i++) {
			cellDisplays[i].setText("0");
		}
		for (int i = 0; i < cells.length; i++) {
			cells[i] = 0;
		}
		outputArea.setText("");
		intialized = false;
	}
	
	private void createAndShowGUI(Stage stage) {
		int ioHeight = 42; //tall enough for two rows without a scrollbar
		
		stage.setTitle("Brainfuck Interpreter");
		stage.setOnCloseRequest((WindowEvent e) -> {
			threadRunning = false;
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
		
		inputArea = new TextArea();
		inputArea.setMinHeight(ioHeight);
		grid.add(inputArea, 0, 1);
		
		Label outputLabel = new Label("OUTPUT");
		GridPane.setHgrow(outputLabel, Priority.ALWAYS);
		grid.add(outputLabel, 1, 0);
		
		outputArea = new TextArea();
		outputArea.setMinHeight(ioHeight);
		grid.add(outputArea, 1, 1);
		
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
		
		sourceArea = new TextArea();
		sourceArea.setPrefHeight(10000); //just has to be big enough to reach the bottom of the stage
		HBox.setHgrow(sourceArea, Priority.ALWAYS);
		sourceControl.getChildren().add(sourceArea);
		
		VBox controls = new VBox(10);
		
		Button[] controlButtons = new Button[8];
		
		controlButtons[0] = new Button("START");
		controlButtons[0].setId("start-button");
		controlButtons[0].setOnAction((ActionEvent e) -> {
			reset();
			initialize();
			programRunning = true;
		});
		
		controlButtons[1] = new Button("STOP");
		controlButtons[1].setId("stop-button");
		controlButtons[1].setOnAction((ActionEvent e) -> {
			programRunning = false;
		});
		
		controlButtons[2] = new Button("STEP");
		controlButtons[2].setOnAction((ActionEvent e) -> {
			step();
		});
		
		controlButtons[3] = new Button("RESET");
		controlButtons[3].setId("reset-button");
		controlButtons[3].setOnAction((ActionEvent e) -> {
			reset();
			programRunning = false;
		});
		
		controlButtons[4] = new Button("SAVE");
		controlButtons[4].setOnAction((ActionEvent e) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Brainfuck Program");
			fileChooser.setInitialDirectory(new File("src/com/jackdahms/bf"));
			File save = fileChooser.showSaveDialog(stage);
			try {
	            FileWriter fileWriter = new FileWriter(save);
	            fileWriter.write(sourceArea.getText());
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
				sourceArea.setText(load.next());
				load.close();
			} catch (Exception e1) {
				System.err.println("Could not load file!");
			}

			program = sourceArea.getText().toCharArray();
			this.input = inputArea.getText().toCharArray();
			for (int i = 0; i < cellDisplays.length; i++) cells[i] = (char)Integer.parseInt(cellDisplays[i].getText());
		});
		
		controlButtons[6] = new Button("15 STEP");
		controlButtons[6].setId("15step-button");
		controlButtons[6].setOnAction((ActionEvent) -> {
			for (int i = 0; i < 15; i++) {
				step();
			}
		});
		
		controlButtons[7] = new Button("RESUME");
		controlButtons[7].setOnAction((ActionEvent) -> {
			if (intialized) {
				programRunning = true;
			}
		});
		
		int[] order = {0, 1, 7, 2, 6, 3, 4, 5}; //order to add the buttons in
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