package gui;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Functions extends Accordion {
	
	AnchorPane critterPane;
	AnchorPane WorldPane;
	Controller controller;
	
	public Functions(Stage s, Controller c) {
		this.controller = c;
		TitledPane CritterPane = new TitledPane("Critter Functions",CritterPane(s));
		this.getPanes().add(CritterPane);


	}
	
	public AnchorPane CritterPane(Stage s) {
		AnchorPane p = new AnchorPane();
		Text t = new Text();
		t.setWrappingWidth(0);
		t.setText("Load a new Critter");
		t.setX(getWidth()/2);
		t.setY(20);
		p.getChildren().add(t);
		Button b = new Button("Browse");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Critters");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		AnchorPane.setTopAnchor(b, 40.0);
		TextField file = new TextField();
		file.setEditable(false);
		AnchorPane.setTopAnchor(file, 80.0);
		b.setOnAction((event) -> {
			File critter = fileChooser.showOpenDialog(s);
			if (critter != null) {
				try {
					String critterPath = critter.getAbsolutePath();
					file.setText(critterPath);
					controller.loadCritter(critterPath);
				} catch (NullPointerException e) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Illegal operation");
					alert.setContentText("You must load a world first");
					alert.showAndWait();
				}
			}

		});
		Button b2 = new Button("Add critter randomly");
		b2.setOnAction(controller::addRandomEntity);

		p.getChildren().add(b);
				p.getChildren().add(file);
		p.getChildren().add(b2);
		p.widthProperty().addListener((observable, old, newNum) -> {
            t.setX(p.getWidth() / 2 - 55);
            b.setLayoutX(p.getWidth() / 2 - 30);
        });
		System.out.println(p.getChildren());
		return p;
	}
	
	
}