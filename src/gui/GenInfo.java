package gui;


import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class GenInfo extends AnchorPane {
	
	private Text text;
	
	public GenInfo() {
		text = new Text();
		this.getChildren().add(text);
	}
	
	public void write(String s) {
		text.setText(text.getText() + "\n" + s);
	}
}