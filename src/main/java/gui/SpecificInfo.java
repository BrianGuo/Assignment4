package gui;

import javafx.beans.InvalidationListener;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import world.Critter;
import world.Entity;
import world.Nothing;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.naming.Binding;

public class SpecificInfo extends AnchorPane implements Observer{

	Controller controller;
	TextFlow info;
	ImageView picture = new ImageView();

	//PropertyBinding critterStatus;


	public SpecificInfo(Controller c) {
		this.controller = c;
//		critterStatus = new PropertyBinding();
		info = new TextFlow();
		info.setLayoutY(60);
		try{
			BufferedReader bu = new BufferedReader(new FileReader("help.txt"));
			String s = bu.readLine();
			Text t = new Text();
			t.setFont(Font.font("System",FontWeight.BOLD,16.0));
			t.setText(s + "\n");
			info.getChildren().add(t);
			s = bu.readLine();
			
			while(s != null) {

				Text t2 = new Text();
				t2.setText(s + "\n");
				info.getChildren().add(t2);
				s = bu.readLine();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		AnchorPane.setTopAnchor(info, 60.0);
		AnchorPane.setLeftAnchor(picture, 30.0);
		AnchorPane.setLeftAnchor(info, 30.0);

		observe(controller);
		this.getChildren().add(picture);
		this.getChildren().add(info);


	}
	public void observe(Observable o) {
		o.addObserver(this);
	}
	
	
	@Override
	public boolean isResizable() {
		return true;
	}
	
	public void update(java.util.Observable o, Object arg) {
		WorldState e = controller.focused.getValue();
		File imagefile = null;
		if( e!= null && e.getRefactored().size() > 0 && !(e.getRefactored().get(0) instanceof Nothing)) { 
			
			switch (e.getRefactored().get(0).getClass().toString()) {
				case "class world.Critter":
					imagefile = new File("critter.png");
					break;
				case "class world.Food":
					imagefile = new File("food2.png");
					break;
				case "class world.Rock":
					imagefile = new File("rock.png");
					break;
				default:
					imagefile = null;
					break;
			}

			try {
				picture.setImage(new Image(new FileInputStream(imagefile)));
				picture.setFitHeight(50);
				picture.setFitWidth(50);
				getChildren().set(0, picture);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}


			info.getChildren().clear();
			for (String s : e.getRefactored().get(0).properties()) {
				Text t = new Text();
				int n = s.indexOf(":");
				t.setFont(Font.font("System", FontWeight.BOLD, 14.0));
				t.setText(s.substring(0, n));
				Text t2 = new Text();
				t2.setText(s.substring(n, s.length()) + "\n");
				info.getChildren().addAll(t, t2);
			}


		}
		else{
			picture.setImage(null);
			info.getChildren().clear();
			displayHelp();
			
		}
	}

	private class PropertyBinding extends ObjectBinding<WorldState>{
		public PropertyBinding(){
			super();
			super.bind(controller.focused);
		}
		@Override
		protected WorldState computeValue() {
			
			if(controller.focused.getValue() != null) {
				return controller.focused.getValue();
			}
			return null;
		}
		public void bind(ObjectProperty<Entity> e){

			super.bind(e);
		}
	}
	private void displayHelp() {
		try{
			BufferedReader bu = new BufferedReader(new FileReader("help.txt"));
			String s = bu.readLine();
			Text t = new Text();
			t.setFont(Font.font("System",FontWeight.BOLD,16.0));
			t.setText(s + "\n");
			info.getChildren().add(t);
			s = bu.readLine();
			
			while(s != null) {
				Text t2 = new Text();
				t2.setText(s + "\n");
				info.getChildren().add(t2);
				s = bu.readLine();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
//	public void bind(ObjectProperty<Entity> e){
//		critterStatus.bind(e);
//	}
}