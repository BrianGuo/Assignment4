package gui;

import java.util.ArrayList;

import world.Entity;

public class WorldState {
	
	private int current_timestep;
	private int current_version_number;
	private double rate;
	private String name;
	private int update_since;
	private int rows;
	private int cols;
	private int population;
	private HexEntity[] state;
	private int[] dead_critters;
	private ArrayList<Entity> refactored;
	
	public HexEntity[] getState() {
		return state;
	}
	
	public void setRefactored(ArrayList<Entity> n) {
		refactored = n;
	}
	
	public int getRows(){
		return rows;
	}
	public int getCols() {
		return cols;
	}
	
	public ArrayList<Entity> getRefactored() {
		return refactored;
	}
	
	public int getCurrentVersion() {
		return current_version_number;
	}
	
	public int getPopulation(){
		return population;
	}
	
	public int getTimestep(){
		return current_timestep;
	}
}