package gui;

import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import world.Factory;
import world.World;

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
			defaultHandler(throwable);
		});
		try{
			//TODO mvc pls.
			Controller controller = new Controller();//
			this.controller = controller;
			AnchorPane pane = (AnchorPane) loader.load();
			RootPane = pane;
			primaryStage.setScene(new Scene(pane,700,700));
			primaryStage.show();
			SplitPane split = (SplitPane) pane.getChildren().get(0);
			SplitPane left = (SplitPane) split.getItems().get(0);

			HexWorld worldPane = new HexWorld(4,3, controller);
			worldPane.widthProperty().addListener(evt -> worldPane.HexPane(worldPane.cols,worldPane.rows));
			worldPane.heightProperty().addListener(evt -> worldPane.HexPane(worldPane.cols,worldPane.rows));
			left.getItems().set(0, worldPane);
			left.setDividerPosition(0, 0.7);

			TabPane pane3 = (TabPane) left.getItems().get(1);
			GenInfo g = new GenInfo(controller);
			g.addWorldTab(Factory.getRandomWorld());
			left.getItems().set(1,g);
			SplitPane right = (SplitPane)split.getItems().get(1);
			right.getItems().set(1, new Functions(primaryStage, controller));
			SpecificInfo spec = new SpecificInfo(controller);
			
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