package gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import world.Entity;

import java.util.ArrayList;

import javax.naming.Binding;

public class SpecificInfo extends AnchorPane {
	
	Controller controller;
	TextFlow info;


	PropertyBinding critterStatus;


	public SpecificInfo(Controller c) {
		this.controller = c;
		critterStatus = new PropertyBinding();
		info = new TextFlow();
		info.setLayoutY(60);
		critterStatus.addListener( new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				info.getChildren().clear();
				for (String s: critterStatus.computeValue()) {
					Text t = new Text();
					int n = s.indexOf(":");
					t.setFont(Font.font("System", FontWeight.BOLD,14.0));
					t.setText(s.substring(0,n));
					Text t2 = new Text();
					t2.setText(s.substring(n,s.length()) + "\n");
					info.getChildren().addAll(t,t2);
				}
				
			}

			

			
		});
		this.getChildren().add(info);
	}


	private class PropertyBinding extends ObjectBinding<ArrayList<String>>{
		public PropertyBinding(){
			super();
			bind(controller.focused);
		}
		@Override
		protected ArrayList<String> computeValue() {
			System.out.println("Checkpoint 2");
			if(controller.focused.getValue() != null) {
				return controller.focused.getValue().properties();
			}
			return null;
		}
		public void bind(ObjectProperty<Entity> e){

			super.bind(e);
		}
	}

	public void bind(ObjectProperty<Entity> e){
		critterStatus.bind(e);
	}
}