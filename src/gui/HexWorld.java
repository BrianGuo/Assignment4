package gui;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;
import world.Coordinate;
import world.Critter;
import world.Entity;

public class HexWorld extends ScrollPane implements Observer {
	
	int rows;
	int cols;
	Controller controller;
	double pressedX, pressedY;
	WorldHex[][] world;
	AnchorPane p ;

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
		this.setContent(p);
		HexPane();
		setVvalue(1.0);
	}
	/*@Override
	public boolean isResizable() {
		return true;
	}*/
	
	
	
	
	public void HexPane(){
		this.cols = controller.getWorldCols();
		this.rows = controller.getWorldRows();
		System.out.println(cols);
		System.out.println(rows);
		world = new WorldHex[cols][rows];
		p.getChildren().clear();
		
		double HexWidth = p.getWidth()/cols;
		p.setMinWidth(HexWidth*cols);
		p.setMinHeight(HexWidth*Math.sqrt(3) * rows/2);
		//double HexWidth = Math.min(getWidth()/cols, getHeight()/(1.15*rows));
		for(int i = 0; i < cols; i++ ){
			for (int j = 0; j < rows; j++ ) {
				if (!(i>= cols || i < 0 || 2*j -i < 0 || 2*j - i >= 2*rows-cols)) {
					double[] cornersX = new double[]{0, HexWidth / 4, HexWidth * 3 / 4, HexWidth, HexWidth * 3 / 4, HexWidth / 4};
					double[] cornersY = new double[]{p.getHeight() - (double) Math.sqrt(3) * HexWidth / 4,
							p.getHeight() - (double) Math.sqrt(3) * HexWidth / 2,
							p.getHeight() - (double) Math.sqrt(3) * HexWidth / 2,
							p.getHeight() - (double) Math.sqrt(3) * HexWidth / 4,
							p.getHeight(), p.getHeight()};
					if (i % 2 == 1) {
						for (int Yoffset = 0; Yoffset < cornersY.length; Yoffset++) {
							cornersY[Yoffset] = cornersY[Yoffset] - HexWidth * Math.sqrt(3) / 4;
						}
					}
					double xTotal = HexWidth*cols;
					double xOffset = (p.getWidth() - xTotal)/2;
					for (int i2 = 0; i2 < cornersX.length; i2++) {
						cornersX[i2] += 0.75 * HexWidth * i;
					}
					double yTotal = HexWidth * Math.sqrt(3) * rows / 2 + HexWidth * Math.sqrt(3) / 4;
					
					double yOffset = (p.getHeight() - yTotal) / 2;
					for (int i3 = 0; i3 < cornersY.length; i3++) {
						cornersY[i3] -= HexWidth * Math.sqrt(3) / 2 * j + yOffset;
						cornersY[i3] += HexWidth * Math.round(i /2.0) * Math.sqrt(3)/2;
					}
					double[] total = new double[12];
					for (int n = 0; n < 6; n++) {
						total[2 * n] = cornersX[n];
						total[2 * n + 1] = cornersY[n];
					}
					WorldHex P2 = new WorldHex(total);
					Entity t = controller.getEntityAt(new Coordinate(i,j));
					if (t == null)
						P2.setFill(Paint.valueOf("White"));
					else{
						double rotate = 0;
						File imagefile = null;
						switch(t.getClass().toString()){
						case "class world.Critter":
							rotate = 60.0*((Critter)t).getDirection();
							imagefile = new File("critter.png");
							break;
						case "class world.Food":
							imagefile = new File("food2.png");
							break;
						case "class world.Rock":
							imagefile = new File("rock.png");
							break;
						default:
							imagefile = new File("critter.png");
							break;
						}
						try{
							Image image = new Image(new FileInputStream(imagefile));
							P2.setFill(new ImagePattern(image,0,0,1,1,true));
						}
						catch(FileNotFoundException e ) {
							e.printStackTrace();
						}
						P2.setRotate(rotate);
					}
					//P2.setFill(Paint.valueOf("White"));
					P2.setStrokeWidth(HexWidth / 20);
					P2.setStroke(Paint.valueOf("Green"));
					P2.setCoordinate(i, j);
					P2.setOnMouseClicked((event) -> {
						if (event.isStillSincePress()) {
							if (event.getButton() == MouseButton.PRIMARY)
								controller.handleFocusClick(event);
							else if (event.getButton() == MouseButton.SECONDARY)
								controller.addEntityClick(event);
						}
					});
					p.getChildren().add(P2);
					world[i][j] = P2;
				}
			}
		}
	}


	@Override
	public void update(Observable o, Object arg) {
		HexPane();
	}
}