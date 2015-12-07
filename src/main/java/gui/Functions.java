package gui;

import java.io.File;
import java.io.FileInputStream;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import world.Food;
import world.Rock;

public class Functions extends Accordion {
	AnchorPane critterPane;
	AnchorPane WorldPane;
	Controller controller;
	
	public Functions(Stage s, Controller c) {
		this.controller = c;
		TitledPane CritterPane = new TitledPane("Critter Functions",CritterPane(s));
		this.getPanes().add(CritterPane);
		TitledPane WorldPane = new TitledPane("World Functions", WorldPane(s));
		this.getPanes().add(WorldPane);
		TitledPane RockPane = new TitledPane("Add Rock", RockPane(s));
		this.getPanes().add(RockPane);
		TitledPane FoodPane = new TitledPane("Add Food", FoodPane(s));
		this.getPanes().add(FoodPane);
		


	}
	
	
	public AnchorPane WorldPane(Stage s) {
		AnchorPane p = new AnchorPane();
		Text t = new Text();
		t.setWrappingWidth(0);
		t.setText("Load a new World");
		t.setY(20);
		p.getChildren().add(t);
		Button b = new Button("Browse");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("World");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));

		AnchorPane.setTopAnchor(b, 40.0);
		TextField file = new TextField();
		file.setEditable(false);
		AnchorPane.setTopAnchor(file, 80.0);
		b.setOnAction((event) -> {
			File world = fileChooser.showOpenDialog(s);
			if (world != null) {
				try {
					file.setText(world.getAbsolutePath());
					controller.loadWorld(world);
				}
				catch(NullPointerException e){
					e.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Illegal operation");
					alert.setContentText("Something went wrong");
					alert.showAndWait();
				}
			}
		});
		p.widthProperty().addListener((observable, old, newNum) -> {
            t.setX(p.getWidth() / 2 - 75);
            b.setLayoutX(p.getWidth() / 2 - 50);
            file.setLayoutX(p.getWidth()/2 - 110);
        });
		p.getChildren().add(b);
		p.getChildren().add(file);
		return p;
	}
	public AnchorPane CritterPane(Stage s) {
		AnchorPane p = new AnchorPane();
		Text t = new Text();
		t.setWrappingWidth(0);
		t.setText("Load a new Critter");
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
		Button b2 = new Button("Add critter randomly");
		b2.setDisable(true);
		b.setOnAction((event) -> {
			File critter = fileChooser.showOpenDialog(s);
			if (critter != null) {
				try {
					String critterPath = critter.getAbsolutePath();
					file.setText(critterPath);
					controller.loadCritter(critterPath);
					b2.setDisable(false);
				} catch (NullPointerException e) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Illegal operation");
					alert.setContentText("You must load a world first");
					alert.showAndWait();
				}
			}

		});
		TextField f2 = new TextField();
		f2.setMaxWidth(40);
		f2.setText("1");
		b2.setOnAction(evt -> controller.addRandomEntity(evt, f2.getText()));
		AnchorPane.setTopAnchor(b2, 120.0);
		p.getChildren().add(b);
		p.getChildren().add(file);
		p.getChildren().add(b2);
		p.getChildren().add(f2);
		AnchorPane.setTopAnchor(f2, 120.0);
		p.widthProperty().addListener((observable, old, newNum) -> {
            t.setX(p.getWidth() / 2 - 85);
            b.setLayoutX(p.getWidth() / 2 - 55);
            b2.setLayoutX(p.getWidth()/2 - 120);
            file.setLayoutX(p.getWidth()/2 - 110);
            f2.setLayoutX(b2.getLayoutX() + 170.0);
        });
		System.out.println(p.getChildren());
		return p;
	}
	
	public AnchorPane RockPane(Stage s) {
		AnchorPane p = new AnchorPane();
		try{
			Image img = new Image(new FileInputStream(new File("rock.png")));
			ImageView imgv = new ImageView(img);
			imgv.setFitHeight(40.0);
			imgv.setFitWidth(40.0);
			ToggleButton rockButton = new ToggleButton("",imgv);
			rockButton.setOnAction(action -> {
				if (rockButton.isSelected())
					controller.loaded = new Rock(0, 0, controller.constants);
				else
					controller.loadCritter(controller.loadedEntity);
			});
			rockButton.setMaxHeight(40.0);
			rockButton.setMaxWidth(40.0);
			p.getChildren().add(rockButton);
			p.widthProperty().addListener(evt -> {
				rockButton.setLayoutX(p.getWidth()/2 - 20);
			});
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return p;
	}
	
	public AnchorPane FoodPane(Stage s) {
		AnchorPane p = new AnchorPane();
		try{
			Image img = new Image(new FileInputStream(new File("food2.png")));
			ImageView imgv = new ImageView(img);
			imgv.setFitHeight(40.0);
			imgv.setFitWidth(40.0);
			ToggleButton foodButton = new ToggleButton("",imgv);
			TextField foodcount = new TextField();
			foodcount.setMaxWidth(100);
			foodcount.setText("100");
			AnchorPane.setTopAnchor(foodcount, 60.0);
			foodButton.setOnAction(action -> {
				if (foodButton.isSelected()){
					int foodnum;
			    	if (foodcount.getText() == null){
			    		Alert alert = new Alert(Alert.AlertType.ERROR);
			        	alert.setTitle("Invalid Field");
			        	alert.setContentText("You must specify a food amount");
			        	alert.showAndWait();
			    	}
			    	else {
			    		try{
			    			foodnum = Integer.parseInt(foodcount.getText());
			    			controller.loaded = new Food(0, 0,foodnum, controller.constants);
			    		}
			    		catch(NumberFormatException e) {
			    			Alert alert = new Alert(Alert.AlertType.ERROR);
			            	alert.setTitle("Invalid Field");
			            	alert.setContentText("You must input a number");
			            	alert.showAndWait();
			    		}
			    	}
				}
				else
					controller.loadCritter(controller.loadedEntity);
			});
			foodButton.setMaxHeight(40.0);
			foodButton.setMaxWidth(40.0);
			p.getChildren().add(foodButton);
			p.getChildren().add(foodcount);
			p.widthProperty().addListener(evt -> {
				foodButton.setLayoutX(p.getWidth()/2 - 20);
				foodcount.setLayoutX(p.getWidth()/2 - 40);
			});
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return p;
	}
	
	
}