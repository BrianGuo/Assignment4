package gui;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Functions extends Accordion {
	
	AnchorPane critterPane;
	AnchorPane WorldPane;
	
	public Functions(Stage s) {
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
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File critter = fileChooser.showOpenDialog(s);
				if (critter != null) {
					String critterPath = critter.getAbsolutePath();
					file.setText(critterPath);
				}
			}
		});
		p.getChildren().add(b);
		p.getChildren().add(file);
		p.widthProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> observable, Number old, Number newNum) {
				t.setX(p.getWidth()/2 - 55);
				b.setLayoutX(p.getWidth()/2 - 30);
			}
		});
		System.out.println(p.getChildren());
		return p;
	}
	
	
}