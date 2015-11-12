package gui;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
		Scene dummyScene = new Scene(this, 400,400);
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
		HexPane(cols, rows);

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
		Scene dummyscene = new Scene(p, getWidth(),getHeight());
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
					double xTotal = 3 * cols * HexWidth / 4 + HexWidth / 4;
					double xOffset = (p.getWidth() - xTotal) / 2;
					for (int i2 = 0; i2 < cornersX.length; i2++) {
						cornersX[i2] += 0.75 * HexWidth * i;
					}
					double yTotal = HexWidth * Math.sqrt(3) * rows / 2 + HexWidth * Math.sqrt(3) / 4;
					System.out.println(p.getHeight());
					double yOffset = (p.getHeight() - yTotal) / 2;
					System.out.println(yOffset);
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
					P2.setFill(Paint.valueOf("White"));
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
				}
			}
		}
		this.setContent(p);
	}
}