package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GenInfo extends StackPane {
	
	private Text text;
	
	public GenInfo() {
		text = new Text();
		this.setAlignment(Pos.TOP_LEFT);
	}
	
	public void write(String s) {
		text.setText(text.getText() + "\n" + s);
	}
}