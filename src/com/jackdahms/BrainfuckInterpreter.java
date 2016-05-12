package com.jackdahms;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BrainfuckInterpreter extends Application {

	/**
	 * TODO
	 * input disselect/unfocus
	 * on resize, update text boxes to remove scroll bars (append and remove space on resize?)
	 */
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) throws Exception {
		stage.setTitle("Brainfuck Interpreter");
        
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 10, 10, 10));
		
//		grid.setGridLinesVisible(true);
		
		Label inputLabel = new Label("INPUT");
		GridPane.setHgrow(inputLabel, Priority.ALWAYS);
		grid.add(inputLabel, 0, 0);
		
		TextArea input = new TextArea();
		input.setMaxHeight(40);
		grid.add(input, 0, 1);
		
		Label outputLabel = new Label("OUTPUT");
		GridPane.setHgrow(outputLabel, Priority.ALWAYS);
		grid.add(outputLabel, 1, 0);
		
		TextArea output = new TextArea();
		output.setMaxHeight(40);
		grid.add(output, 1, 1);
		
		Label memoryLabel = new Label("MEMORY");
		grid.add(memoryLabel, 0, 2);
		
		
		HBox memory = new HBox(10);
		TextField[] cells = new TextField[20];
		for (int i = 0; i < cells.length; i++) {
			cells[i] = new TextField("0");
			cells[i].setFont(Font.font("monospace", 12));
			memory.getChildren().add(cells[i]);
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
		
		Button startButton = new Button("START");
		Button stopButton = new Button("STOP");
		Button stepButton = new Button("STEP");
		Button resetButton = new Button("RESET");
		Button saveButton = new Button("SAVE");
		Button loadButton = new Button("LOAD");
		
		controls.getChildren().addAll(startButton, stopButton, stepButton, resetButton, saveButton, loadButton);
		sourceControl.getChildren().add(controls);
		
		grid.add(sourceControl, 0, 5, 2, 1);

		Scene scene = new Scene(grid, 1000, 600);
		try {
			scene.getStylesheets().add(BrainfuckInterpreter.class.getResource("/com/jackdahms/brainfuck.css").toExternalForm());
		} catch (Exception e) {
			System.err.println("Could not load stylesheet...");
		}
		stage.setScene(scene);
        stage.show();
	}
	
	
}