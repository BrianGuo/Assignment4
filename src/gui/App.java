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
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class App extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@FXML
	AnchorPane WorldPane;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Hi");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Test.fxml"));
		try{
			//TODO mvc pls.
			Controller controller;//
			AnchorPane pane = (AnchorPane) loader.load();
			primaryStage.setScene(new Scene(pane,700,700));
			primaryStage.show();
			SplitPane split = (SplitPane) pane.getChildren().get(0);
			SplitPane left = (SplitPane) split.getItems().get(0);
			AnchorPane worldPane = (AnchorPane) left.getItems().get(0);
			/*double[] cornersX = new double[]{0,50/4,50*3/4,50,50*3/4,50/4};
			double[] cornersY = new double[]{100 - (double)Math.sqrt(3) * 50/4, 100 - (double)Math.sqrt(3) * 50 / 2, 100- (double)Math.sqrt(3) * 50/2,  100 - (double)Math.sqrt(3) * 50/4, 100, 100};
			double[] total = new double[12];
			for (int n = 0; n < 6; n++){
				total[2*n] = cornersX[n];
				total[2*n+1] = cornersY[n];
			}
			Polygon hex = new Polygon(total);
			worldPane.getChildren().add(hex);*/
			/*Hexgrid grid = new Hexgrid(4,3,1500);
			grid.repaint();
			grid.widthProperty().bind(worldPane.widthProperty());
			grid.heightProperty().bind(worldPane.heightProperty());
			worldPane.getChildren().add(grid);
			System.out.println(worldPane.widthProperty().doubleValue());*/
			HexPane(worldPane,10,3);
			System.out.println(worldPane.getChildren());
			AnchorPane pane3 = (AnchorPane) left.getItems().get(1);
			GenInfo g = new GenInfo();
			g.write("Hello");
			left.getItems().set(1,g);
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		/*Hexgrid grid = new Hexgrid(4,3,10);
		System.out.println(WorldPane);
		WorldPane.getChildren().add(grid);
		primaryStage.setTitle("HexGrid");
		Scene scene = new Scene(WorldPane);
		primaryStage.setScene(scene);*/
	}
	
	public void HexPane(AnchorPane P, int cols, int rows){
		System.out.println(P.getWidth());
		double HexWidth = P.getWidth()/cols;
		for(int i = 0; i < cols; i++ ){
			for (int j = 0; j < rows; j++ ) {
				double[] cornersX = new double[]{0,HexWidth/4,HexWidth*3/4,HexWidth,HexWidth*3/4,HexWidth/4};
				double[] cornersY = new double[]{P.getHeight() - (double)Math.sqrt(3) * HexWidth/4,
						P.getHeight() - (double)Math.sqrt(3) * HexWidth / 2,
						P.getHeight()- (double)Math.sqrt(3) * HexWidth/2,
						P.getHeight() - (double)Math.sqrt(3) * HexWidth/4,
						P.getHeight(), P.getHeight()};
				if (i % 2 == 1){
					for (int Yoffset = 0; Yoffset < cornersY.length; Yoffset++ ){
						cornersY[Yoffset] = cornersY[Yoffset] - HexWidth*Math.sqrt(3)/4;
					}
				}
				double xTotal = 3*cols*HexWidth/4 + HexWidth/4;
				double xOffset = (P.getWidth() - xTotal)/2;
				for(int i2 = 0; i2 < cornersX.length;i2++) {
					cornersX[i2] += 0.75 * HexWidth*i + xOffset;
				}
				double yTotal = HexWidth*Math.sqrt(3)*rows/2 + HexWidth*Math.sqrt(3)/4;
				double yOffset = (P.getHeight() - yTotal)/2;
				for(int i3 = 0; i3< cornersY.length; i3++) {
					cornersY[i3] -= HexWidth*Math.sqrt(3)/2*j + yOffset;
				}
				double[] total = new double[12];
				for (int n = 0; n < 6; n++){
					total[2*n] = cornersX[n];
					total[2*n+1] = cornersY[n];
				}
				WorldHex P2 = new WorldHex(total);
				P2.setFill(Paint.valueOf("White"));
				P2.setStrokeWidth(HexWidth / 20);
				System.out.println(P.maxHeight(300));
				//P2.relocate(xOffset, yOffset);
				P2.setStroke(Paint.valueOf("Green"));
				P2.setCoordinate(i, j);
				P2.setOnMouseClicked(event -> {

                });


				P.getChildren().add(P2);
			}
		}
	}
}