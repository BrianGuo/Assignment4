package gui;

import world.Entity;

public class WorldState {
	
	private int current_timestep;
	private int current_version;
	private double rate;
	private String name;
	private int update_since;
	private int rows;
	private int cols;
	private Entity[] state;
	private int[] dead_critters;
	
	public Entity[] getState() {
		return state;
	}
}