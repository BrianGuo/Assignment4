package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Hexgrid extends Canvas {
	
	private int rows;
	private int cols;
	private double HexWidth;
	public Hexgrid(int cols, int rows, int HexWidth) {
		this.rows = rows;
		this.cols = cols;
		widthProperty().addListener(evt -> repaint());
		heightProperty().addListener(evt -> repaint());
	}
	
	@Override
	public boolean isResizable() {
		return true;
	}
	
	public
	void repaint() {
		GraphicsContext g = this.getGraphicsContext2D();
		g.clearRect(0, 0, getWidth(), getHeight());
		this.HexWidth = getWidth()/cols  ;
		for(int i = 0; i < cols; i++ ){
			for (int j = 0; j < rows; j++ ) {
				double[] cornersX = new double[]{0,HexWidth/4,HexWidth*3/4,HexWidth,HexWidth*3/4,HexWidth/4};
				double[] cornersY = new double[]{getHeight() - (double)Math.sqrt(3) * HexWidth/4, getHeight() - (double)Math.sqrt(3) * HexWidth / 2, getHeight()- (double)Math.sqrt(3) * HexWidth/2,  getHeight() - (double)Math.sqrt(3) * HexWidth/4, getHeight(), getHeight()};
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
				g.setLineWidth(HexWidth/20);
				g.setStroke(Paint.valueOf("Green"));
				g.strokePolygon(cornersX, cornersY, 6);
			}
		}
	}
}