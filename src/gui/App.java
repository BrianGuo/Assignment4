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
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import world.World;

public class App extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	AnchorPane WorldPane;
	TabPane GenInfoPane;
	AnchorPane TextPane;
	Accordion FunctionsPane;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Hi");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Test.fxml"));
		try{
			//TODO mvc pls.
			Controller controller = new Controller();//
			AnchorPane pane = (AnchorPane) loader.load();
			primaryStage.setScene(new Scene(pane,700,700));
			primaryStage.show();
			SplitPane split = (SplitPane) pane.getChildren().get(0);
			SplitPane left = (SplitPane) split.getItems().get(0);
			HexWorld worldPane = new HexWorld(4,3, controller);
			this.WorldPane = worldPane;
			worldPane.widthProperty().addListener(evt -> worldPane.HexPane(worldPane.cols,worldPane.rows));
			worldPane.heightProperty().addListener(evt -> worldPane.HexPane(worldPane.cols,worldPane.rows));
			left.getItems().set(0, worldPane);
			left.setDividerPosition(0, 0.7);
			TabPane pane3 = (TabPane) left.getItems().get(1);
			GenInfo g = new GenInfo();
			this.GenInfoPane = g;
			g.addWorldTab(new World());
			left.getItems().set(1,g);
			SplitPane right = (SplitPane)split.getItems().get(1);
			right.getItems().set(1, new Functions(primaryStage));
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}