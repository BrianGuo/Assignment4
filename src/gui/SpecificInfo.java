package gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import world.Entity;

public class SpecificInfo extends AnchorPane {
	
	Controller controller;
	Text info;
	ObjectProperty<Entity> e;
	
	public SpecificInfo(Controller c) {
		this.controller = c;
		info = new Text();
		info.setY(20);
		e = c.focused;
	}
	
	public void write(String s) {
		info.setText(info.getText() + "\n" + s);
	}
	
	
}