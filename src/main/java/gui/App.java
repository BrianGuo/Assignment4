package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import world.Factory;
import world.World;

import java.io.File;
import java.util.Optional;

public class App extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	AnchorPane RootPane;
	Controller controller;
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Hi");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Test.fxml"));
		Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
			System.out.println("Handler caught exception: " + throwable.getMessage());
			System.out.println(throwable.getCause());
			System.out.println(throwable.toString());
			defaultHandler(throwable);
		});
		try{
			//TODO mvc pls.
			Controller controller = new Controller();//
			this.controller = controller;
			AnchorPane pane = (AnchorPane) loader.load();
			RootPane = pane;
			Scene scene = new Scene(pane, 900, 700);
			File f = new File("style.css");
			scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
			System.out.println(scene.getStylesheets());
			primaryStage.setScene(scene);

			primaryStage.show();
			SplitPane split = (SplitPane) pane.getChildren().get(0);
			SplitPane left = (SplitPane) split.getItems().get(0);


			World w = Factory.getRandomWorld();
			controller.setWorld(w);
			Dialog<Pair<String,String>> dialog = new Dialog<>();
			dialog.setTitle("Login Dialog");
			dialog.setHeaderText("Please log in");
			ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(loginButtonType);
			dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));
			TextField level = new TextField();
			level.setPromptText("level");
			PasswordField password = new PasswordField();
			password.setPromptText("Password");
			grid.add(new Label("level:"), 0, 0);
			grid.add(level, 1, 0);
			grid.add(new Label("Password:"), 0, 1);
			grid.add(password, 1, 1);
			//Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
			//loginButton.setDisable(true);
			dialog.getDialogPane().setContent(grid);
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == loginButtonType) {
			        return new Pair<>(level.getText(), password.getText());
			    }
			    return null;
			});
			Optional<Pair<String,String>> result = dialog.showAndWait();
			controller.validate(result);
		
			HexWorld worldPane = new HexWorld(6,8, controller);

			controller.addObserver(worldPane);
			/*worldPane.widthProperty().addListener(evt -> worldPane.HexPane(worldPane.cols,worldPane.rows));
			worldPane.heightProperty().addListener(evt -> worldPane.HexPane(worldPane.cols,worldPane.rows));*/


			left.getItems().set(0, worldPane);
			left.setDividerPosition(0, 0.7);

			TabPane pane3 = (TabPane) left.getItems().get(1);
			GenInfo g = new GenInfo(controller);
			g.addWorldTab(Factory.getRandomWorld());
			left.getItems().set(1,g);
			SplitPane right = (SplitPane)split.getItems().get(1);
			right.getItems().set(1, new Functions(primaryStage, controller));
			SpecificInfo spec = new SpecificInfo(controller);
			right.getItems().set(0, spec);
		}
			catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	public void redrawWorld(int cols, int rows) {
		HexWorld worldPane = new HexWorld(cols,rows,controller);
	}
	private void defaultHandler(Throwable throwable) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Illegal operation");
		alert.setContentText(throwable.getMessage());
		alert.showAndWait();
	}
}