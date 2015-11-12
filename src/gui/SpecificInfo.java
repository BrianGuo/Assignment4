package gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import world.Entity;

import java.util.ArrayList;

public class SpecificInfo extends AnchorPane {
	
	Controller controller;
	Text info;


	CritterBinding critterStatus;


	public SpecificInfo(Controller c) {
		this.controller = c;
		critterStatus = new CritterBinding();
		info = new Text();
		info.setY(20);
		info.textProperty().bind(critterStatus);
	}


	private class CritterBinding extends StringBinding{
		public CritterBinding(){
			super();
			bind(controller.focused);
		}
		@Override
		protected String computeValue() {
			String cur = "";
			if(controller.focused.getValue() != null) {
				ArrayList<String> properties = controller.focused.getValue().properties();
				for (String property : properties) {
					cur += property + "\n";
				}
			}
			return cur;
		}
		public void bind(ObjectProperty<Entity> e){

			super.bind(e);
		}
	}

	public void bind(ObjectProperty<Entity> e){
		critterStatus.bind(e);
	}
}