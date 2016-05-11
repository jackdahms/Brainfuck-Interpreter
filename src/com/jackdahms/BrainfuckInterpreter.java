package com.jackdahms;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class BrainfuckInterpreter extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) throws Exception {
		stage.setTitle("JavaFX Welcome");
        
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));
		
		grid.setGridLinesVisible(true);
		
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
		grid.add(output, 1, 1);

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