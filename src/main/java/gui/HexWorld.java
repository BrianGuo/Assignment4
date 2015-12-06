package gui;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import exceptions.IllegalOperationException;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import world.Critter;
import world.Entity;
import world.Nothing;

public class HexWorld extends ScrollPane implements Observer {
	
	int rows;
	int cols;
	Controller controller;
	double pressedX, pressedY;
	WorldHex[][] world;
	AnchorPane p ;
	Image critterImage;
	Image rockImage;
	Image foodImage;

	public HexWorld(int c, int r, Controller controller){
		rows = r;
		cols = c;
		this.controller = controller;
		setPannable(true);
		Scene dummyScene = new Scene(this, 900,900);
		p = new AnchorPane();
		Scene dummyscene = new Scene(p, getWidth(),getHeight());
		this.setOnMousePressed(event -> {
			pressedX = event.getX();
			pressedY = event.getY();
		});
		this.setOnMouseDragged(event -> {
			setTranslateX(getTranslateX() + event.getX() - pressedX);
			setTranslateY(getTranslateY() + event.getY() - pressedY);
			event.consume();
		});
		try {
			critterImage = new Image(new FileInputStream(new File("critter.png")));
			rockImage = new Image(new FileInputStream(new File("rock.png")));
			foodImage = new Image(new FileInputStream(new File("food2.png")));
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			throw new IllegalOperationException("Image files not found!");

		}
		setContent(p);
		HexPane();
		setVvalue(1.0);
		setHvalue(0.0);
		
	}
	/*@Override
	public boolean isResizable() {
		return true;
	}*/
	
	
	
	
	public void HexPane(){
		WorldState m = controller.updateWorld();
		if (m.getCols()!= this.cols || m.getRows() != this.rows){
			this.cols = m.getCols();
			this.rows = m.getRows();
			world = new WorldHex[cols][rows];
			p.getChildren().clear();
			
			double HexWidth = p.getWidth()/cols;
			p.setPrefWidth(HexWidth*cols);
			//double HexWidth = Math.min(getWidth()/cols, getHeight()/(1.15*rows));
			for(int i = 0; i < cols; i++ ){
				for (int j = 0; j < rows; j++ ) {
					if (!(i>= cols || i < 0 || 2*j -i < 0 || 2*j - i >= 2*rows-cols)) {
						drawHex(i,j,HexWidth,null);
					}
				}
			}
		}
		ArrayList<Entity> diff = m.getRefactored();
		double HexWidth = p.getWidth()/cols;
		for (Entity e: diff)
			drawHex(e.getCol(),e.getRow(),HexWidth, e);
		
	}

	public void drawHex(int col, int row, double HexWidth, Entity e) {
		double[] cornersX = new double[]{0, HexWidth / 4, HexWidth * 3 / 4, HexWidth, HexWidth * 3 / 4, HexWidth / 4};
		double[] cornersY = new double[]{p.getHeight()-5 - (double) Math.sqrt(3) * HexWidth / 4,
				p.getHeight()-5 - (double) Math.sqrt(3) * HexWidth / 2,
				p.getHeight()-5 - (double) Math.sqrt(3) * HexWidth / 2,
				p.getHeight()-5 - (double) Math.sqrt(3) * HexWidth / 4,
				p.getHeight()-5, p.getHeight()-5};
		if (col % 2 == 1) {
			for (int Yoffset = 0; Yoffset < cornersY.length; Yoffset++) {
				cornersY[Yoffset] = cornersY[Yoffset] - HexWidth * Math.sqrt(3) / 4;
			}
		}
		double xTotal = HexWidth*cols;
		double xOffset = (p.getWidth() - xTotal)/2;
		for (int i2 = 0; i2 < cornersX.length; i2++) {
			cornersX[i2] += 0.75 * HexWidth * col;
		}
		double yTotal = HexWidth * Math.sqrt(3) * rows / 2 + HexWidth * Math.sqrt(3) / 4;
		
		for (int i3 = 0; i3 < cornersY.length; i3++) {
			cornersY[i3] -= HexWidth * Math.sqrt(3) / 2 * row;
			cornersY[i3] += HexWidth * Math.round(col /2.0) * Math.sqrt(3)/2;
		}
		double[] total = new double[12];
		for (int n = 0; n < 6; n++) {
			total[2 * n] = cornersX[n];
			total[2 * n + 1] = cornersY[n];
		}
		WorldHex P2 = new WorldHex(total);
		if (e == null || e instanceof Nothing)
			P2.setFill(Paint.valueOf("White"));
		else{
			double rotate = 0;
			//File imagefile = null;
			switch(e.getClass().toString()){
			case "class world.Critter":
				rotate = 60.0*((Critter)e).getDirection();

				P2.setFill(new ImagePattern(critterImage, 0, 0, 1, 1, true));
				break;
			case "class world.Food":
				P2.setFill(new ImagePattern(foodImage, 0, 0, 1, 1, true));
				break;
			case "class world.Rock":
				P2.setFill(new ImagePattern(rockImage, 0, 0, 1, 1, true));
				break;
			default:
				break;
			}

			P2.setRotate(rotate);
		}
		//P2.setFill(Paint.valueOf("White"));
		P2.setStrokeWidth(HexWidth / 20);
		P2.setStroke(Paint.valueOf("Green"));
		P2.setCoordinate(col, row);
		P2.setOnMouseClicked((event) -> {
			if (event.isStillSincePress()) {
				if (event.getButton() == MouseButton.PRIMARY)
					controller.handleFocusClick(event);
				else if (event.getButton() == MouseButton.SECONDARY)
					controller.addEntityClick(event);
			}
		});
		p.getChildren().add(P2);
		world[col][row] = P2;
	}

	@Override
	public void update(Observable o, Object arg) {
		p.setMinHeight(p.getHeight());
		HexPane();
	}
}