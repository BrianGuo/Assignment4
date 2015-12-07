package gui;

import java.util.ArrayList;

import world.Entity;

public class HexEntity {
	
	private int row;
	private int col;
	private String type;
	private int value;
	private int id;
	private String species_id;
	private int direction;
	private int[] mem;
	private int recently_executed_rule;
	private String program;
	

	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public String getType() {
		return type;
	}
	
	public int getValue(){
		return value;
	}
	public int getID(){
		return id;
	}
	public String getSpecID() {
		return species_id;
	}
	public int[] getMem() {
		return mem;
	}
	public int getDirection() {
		return direction;
	}
	public int getRecentlyExecutedRule() {
		return recently_executed_rule;
	}
	
	public String getProgram() {
		return program;
	}
	
}