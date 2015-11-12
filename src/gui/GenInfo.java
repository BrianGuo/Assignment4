package gui;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import world.Critter;
import world.World;

public class GenInfo extends TabPane implements Observer{
	private ArrayList<String> info;
	private Text generalInfo;
	private Critter critter;
	private World world;
	Controller controller;
	SimpleIntegerProperty timesteps = new SimpleIntegerProperty(0);
	SimpleIntegerProperty numCritters = new SimpleIntegerProperty(0);

	StringBinding status = new StringBinding(){
		{
			super.bind(timesteps, numCritters);
		}
		protected String computeValue(){
			String cur = "";
			cur += "Number of critters: " + numCritters.getValue() + "\n";
			cur += "Timestep: " + timesteps.getValue() + "\n";
			return cur;
		}
	};



	//...why does a TabPane also handle a single tab?...
	
	public GenInfo(Controller c) {

		controller = c;
		timesteps.set(0);
		numCritters.set(0);

		generalInfo = new Text();
		generalInfo.textProperty().bind(status);

		Tab t = new Tab();
		info = new ArrayList<String>();
		t.setText("World status");
		t.setContent(generalInfo);
		this.getTabs().add(t);
		observe(controller);

		//Label l = new Label();

	}

	public void observe(Observable o) {
		o.addObserver(this);
	}
	/**
	 * Sets the current number of critters to n
	 * @param n Number of critters
	 */
	public void  updateCritters(int n){
		numCritters.set(n);
	}

	/**
	 * Sets current timesteps to n
	 * @param n Current timestep
	 */
	public void updateTimesteps(int n) {
		timesteps.set(n);
	}

	public void addCritterTab(Critter c) {
	}
	
	public void addWorldTab(World w) {
		Tab worldTab = new Tab();
		worldTab.setText(w.name);
		Text worldInfo = new Text();
		ArrayList<String> info = new ArrayList<>();
		info.add("The size of the world is " + w.getColumns() + "x" + w.getRows());
		String cur = "";
		for(String str: info){
			cur += str;
			cur += "\n";
		}

		worldInfo.setText(cur);
		worldTab.setContent(worldInfo);
		getTabs().add(worldTab);
	}

	@Override
	public void update(Observable o, Object arg) {
		updateTimesteps(controller.sim.getTimesteps());
		updateCritters(controller.sim.getNumCritters());
	}
}