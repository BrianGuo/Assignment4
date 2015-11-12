package gui;


import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

public class HexWorld extends ScrollPane {
	
	int rows;
	int cols;
	Controller controller;
	double pressedX, pressedY;

	public HexWorld(int c, int r, Controller controller){
		rows = r;
		cols = c;
		this.controller = controller;
		setPannable(true);
		System.out.println(cols);
		System.out.println(rows);
		this.setOnMousePressed(event -> {
            pressedX = event.getX();
            pressedY = event.getY();
        });
		this.setOnMouseDragged(event -> {
            setTranslateX(getTranslateX() + event.getX() - pressedX);
            setTranslateY(getTranslateY() + event.getY() - pressedY);

            event.consume();
        });
		this.
		HexPane(cols,rows);

	}
	/*@Override
	public boolean isResizable() {
		return true;
	}*/
	
	
	public void Heights(double D) {
		System.out.println("Max Height: " + this.getMaxHeight());
		System.out.println("Set to :" + D);
		setMaxHeight(D);
	}
	
	public void HexPane(int cols, int rows){
		getChildren().clear();
		double HexWidth = 100;
		AnchorPane p = new AnchorPane();
		//double HexWidth = Math.min(getWidth()/cols, getHeight()/(1.15*rows));
		for(int i = 0; i < cols; i++ ){
			for (int j = 0; j < rows; j++ ) {
				double[] cornersX = new double[]{0,HexWidth/4,HexWidth*3/4,HexWidth,HexWidth*3/4,HexWidth/4};
				double[] cornersY = new double[]{getHeight() - (double)Math.sqrt(3) * HexWidth/4,
						getHeight() - (double)Math.sqrt(3) * HexWidth / 2,
						getHeight()- (double)Math.sqrt(3) * HexWidth/2,
						getHeight() - (double)Math.sqrt(3) * HexWidth/4,
						getHeight(), getHeight()};
				if (i % 2 == 1){
					for (int Yoffset = 0; Yoffset < cornersY.length; Yoffset++ ){
						cornersY[Yoffset] = cornersY[Yoffset] - HexWidth*Math.sqrt(3)/4;
					}
				}
				double xTotal = 3*cols*HexWidth/4 + HexWidth/4;
				double xOffset = (getWidth() - xTotal)/2;
				for(int i2 = 0; i2 < cornersX.length;i2++) {
					cornersX[i2] += 0.75 * HexWidth*i + xOffset;
				}
				double yTotal = HexWidth*Math.sqrt(3)*rows/2 + HexWidth*Math.sqrt(3)/4;
				double yOffset = (getHeight() - yTotal)/2;
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
				P2.setStroke(Paint.valueOf("Green"));
				P2.setCoordinate(i, j);
				P2.setOnMouseClicked((event) ->{
					if(event.isStillSincePress()){
						controller.handleFocusClick(event);
					}
				});


				p.getChildren().add(P2);
			}
		}
		this.setContent(p);
	}
}