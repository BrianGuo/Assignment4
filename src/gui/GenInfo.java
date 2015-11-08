package gui;


import java.util.ArrayList;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import world.Critter;
import world.World;

public class GenInfo extends TabPane {
	private ArrayList<String> info;
	private Text GeneralInfo;
	private Critter critter;
	private World world;
	
	
	public GenInfo() {
		Tab t = new Tab();
		GeneralInfo = new Text();
		info = new ArrayList<String>();
		info.add("");
		info.add("");
		t.setText("Hello");
		this.getTabs().add(t);
	}
	
	public void addInfo (String s) {
		info.add(s + "\n");
		rewrite();
	}
	public void setLiveCritters(String s) {
		String result = "The number of Critters Alive is " + s;
		info.set(1, result);
		rewrite();
	}
	public void setTimesteps(String s) {
		String result = "The number of timesteps is " + s;
		info.set(0, result);
		rewrite();
	}
	public void rewrite() {
		GeneralInfo.setText("");
		for(String i: info) {
			GeneralInfo.setText(GeneralInfo.getText() + "\n" + i);
		}
	}
	
	public void addCritterTab(Critter c) {
	}
	
	public void addWorldTab(World w) {
		Tab worldTab = new Tab();
		worldTab.setText("World");
		Text WorldInfo = new Text();
		ArrayList<String> Info = new ArrayList<String>();
		Info.add("The size of the world is " + w.getColumns() + "x" + w.getRows());
		for (String i: Info) {
			WorldInfo.setText(WorldInfo.getText() + i + "\n" );
		}
		worldTab.setContent(WorldInfo);
		getTabs().add(worldTab);
	}
}